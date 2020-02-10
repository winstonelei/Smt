package com.github.parallelJdbc;

import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * jdbcutils
 */
public class JdbcUtils {

    public static void closeQuietly(ResultSet rs, Statement st, Connection connection) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ignore) {
        }
    }

    public static void closeQuietly(Statement st, Connection connection) {
        try {
            if (st != null) {
                st.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ignore) {
        }
    }

    public static List<Integer> getIds(PreparedStatement ps) throws SQLException {
        List<Integer> ids = new ArrayList();

        ResultSet rs = null;
        try {
            rs = ps.getGeneratedKeys();
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return ids;
    }

    public static Integer getId(PreparedStatement ps) throws SQLException {
        ResultSet rs = null;
        try {
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return null;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static JSONObject toJSONObject(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        JSONObject data = new JSONObject();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            switch (metaData.getColumnType(i)) {
                case Types.DATE:
                   // data.put(columnName, DateUtils2.formatMedium(rs.getDate(i)));
                    break;
                case Types.TIME:
                    data.put(columnName, rs.getTime(i));
                    break;
                case Types.TIMESTAMP:
                    //data.put(columnName, DateUtils2.formatFull(rs.getTimestamp(i)));
                    break;
                case Types.BIGINT:
                case Types.DECIMAL:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.INTEGER:
                case Types.NUMERIC:
                case Types.SMALLINT:
                case Types.TINYINT:
                case Types.REAL://MySQL中的Float类型会被JDBC识别为REAL类型
                    data.put(columnName, rs.getString(i));
                    break;
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    data.put(columnName, rs.getObject(i));
                    break;
                case Types.BIT:
                    final boolean v = rs.getBoolean(i);
                    data.put(columnName, rs.wasNull() ? null : v);
                    break;
                default:
                    throw new JdbcException(String.format("不支持java.sql.Types中类型值为[%s]的字段类型", metaData.getColumnType(i)));
            }
        }
        return data;
    }
}
