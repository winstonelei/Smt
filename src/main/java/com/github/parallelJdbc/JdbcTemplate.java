package com.github.parallelJdbc;

import com.alibaba.fastjson.JSONObject;
import com.github.collections.CollectionsUtils2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static com.github.parallelJdbc.JdbcUtils.closeQuietly;

/**
 * //todo: 增加必要的参数检查
 *
 */
public class JdbcTemplate {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    private static final int BATCH_SIZE = 500;

    private DataSource dataSource;

    public JdbcTemplate(DatabaseInfo databaseInfo) {
        this.dataSource = DataSourceFactory.getDataSource(databaseInfo);
    }

    public List<JSONObject> query(String sql) {
        logger.debug("执行查询SQL:{}", sql);
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        List<JSONObject> resultList = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                resultList.add(JdbcUtils.toJSONObject(rs));
            }

        } catch (Exception e) {
            logger.error("执行SQL出错,SQL:{},异常信息:{}", sql, ExceptionUtils.getStackTrace(e));
            throw new JdbcException(e);
        } finally {
            JdbcUtils.closeQuietly(rs, st, connection);
        }

        return resultList;
    }

    public List<JSONObject> query(String sql, Object... params) {
        logger.debug("执行SQL:{},params:{}", sql, CollectionsUtils2.toString(params));
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<JSONObject> resultList = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            st = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                st.setObject(i + 1, params[i]);
            }
            rs = st.executeQuery();

            while (rs.next()) {
                resultList.add(JdbcUtils.toJSONObject(rs));
            }

        } catch (Exception e) {
            logger.error("SQL:{},params:{}", sql, params, CollectionsUtils2.toString(params));
            throw new JdbcException(e);
        } finally {
            JdbcUtils.closeQuietly(rs, st, connection);
        }

        return resultList;
    }

    /**
     * 插入操作.如果插入成功,且有主键生成,则返回值为新插入记录的主键值,否则返回null.
     *
     * @param sql
     * @param params
     * @return
     */
    public Integer insert(String sql, Object... params) {
        return executeUpdate(sql, params);
    }

    /**
     * 批量插入操作.如果插入成功,且有主键生成,则返回值为新插入记录的主键值的列表,否则返回null.
     *
     * @param sql
     * @param params
     * @return
     */
    public long insertBatch(String sql, List<Object[]> params) {
        return executeBatchUpdate(sql, params);
    }

    /**
     * 批量插入操作,根据表名和JSON数据,自动生成insert语句.
     * 注意,dataList中的JSON,应该是从同一个SQL中查询出来的,以保证所有数据的列是一致的
     *
     * @param tableName
     * @param dataList
     * @return preparedStatement.executeBatch所花费的时间
     */
    public long insertAllColumnBatch(String tableName, List<JSONObject> dataList) {
    /*    if (CollectionsUtils2.isEmpty(dataList)) {
            throw new IllegalArgumentException("dataList不能为空");
        }*/

        List<String> columnList = new ArrayList();
        JSONObject firstData = dataList.get(0);
        StringBuffer insertBuffer = new StringBuffer("insert into ").append(tableName).append("(");
        StringBuffer valueBuffer = new StringBuffer(" values").append("(");
        firstData.entrySet().forEach(entry -> {
            insertBuffer.append(entry.getKey()).append(",");
            valueBuffer.append("?").append(",");
            columnList.add(entry.getKey());
        });
        insertBuffer.deleteCharAt(insertBuffer.length() - 1).append(")");
        valueBuffer.deleteCharAt(valueBuffer.length() - 1).append(")");
        String sql = insertBuffer.append(valueBuffer).toString();

        List<Object[]> valueDataList = new ArrayList(dataList.size());
        dataList.forEach(data -> {
            List<Object> e = new ArrayList();
            columnList.forEach(column -> {
                e.add(data.get(column));
            });
            valueDataList.add(e.toArray());
        });

        return executeBatchUpdate(sql, valueDataList);
    }

    public void update(String sql, Object... params) {
        executeUpdate(sql, params);
    }

    public long updateBatch(String sql, List<Object[]> params) {
        return executeBatchUpdate(sql, params);
    }

    public void delete(String sql, Object... params) {
        executeUpdate(sql, params);
    }

    public long deleteBatch(String sql, List<Object[]> params) {
        return executeBatchUpdate(sql, params);
    }

    public void executeDDL(String ddl) {
        logger.debug("执行DDL:{}", ddl);
        Connection connection = null;
        Statement st = null;

        try {
            connection = dataSource.getConnection();
            st = connection.createStatement();
            st.executeUpdate(ddl);
        } catch (Exception e) {
            logger.error("DDL:{}", ddl);
            throw new JdbcException(e);
        } finally {
            closeQuietly(st, connection);
        }
    }

    private Integer executeUpdate(String sql, Object... params) {
        logger.debug("执行SQL:{},params:{}", sql, CollectionsUtils2.toString(params));
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dataSource.getConnection();
            ps = connection.prepareCall(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();

            return JdbcUtils.getId(ps);
        } catch (Exception e) {
            logger.error("SQL:{},params:", sql, CollectionsUtils2.toString(params));
            throw new JdbcException(e);
        } finally {
            closeQuietly(ps, connection);
        }

    }

    private long executeBatchUpdate(String sql, List<Object[]> params) {
        logger.trace("执行插入SQL:{}", sql);
        Connection connection = null;
        PreparedStatement ps = null;

        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareCall(sql);

            int index = 0;
            for (Iterator<Object[]> iterator = params.iterator(); iterator.hasNext(); ) {
                index++;
                Object[] pa = iterator.next();
                for (int j = 0; j < pa.length; j++) {
                    ps.setObject(j + 1, pa[j]);
                }
                ps.addBatch();

                if (index == BATCH_SIZE) {
                    watch.resume();
                    ps.executeBatch();
                    index = 0;
                    connection.commit();
                    watch.suspend();
                }
            }
            if (index != 0 && index != BATCH_SIZE) {
                watch.resume();
                ps.executeBatch();
                connection.commit();
            }

            return watch.getTime();
        } catch (Exception e) {
            logger.error("SQL:{},params:{}", sql, CollectionsUtils2.toString(params));
            throw new JdbcException(e);
        } finally {
            closeQuietly(ps, connection);
        }

    }

    public static void destroy() {
        DataSourceFactory.destroy();
    }

}
