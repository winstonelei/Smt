package com.github.parallelJdbc;

import com.github.collections.StringUtils2;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class DataSourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    private static Map<String, DataSource> dataSourceMap = new ConcurrentHashMap();

    public synchronized static DataSource getDataSource(DatabaseInfo databaseInfo) {
        String key = buildUrl(databaseInfo);
        if (dataSourceMap.containsKey(key)) {
            return dataSourceMap.get(key);
        } else {
            logger.info("初始化数据源:{}", key);
            DataSource newDataSource = createDataSource(databaseInfo);
            dataSourceMap.put(key, newDataSource);
            return newDataSource;
        }
    }

    private static DataSource createDataSource(DatabaseInfo databaseInfo) {
        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setUrl(buildUrl(databaseInfo));
        poolProperties.setDriverClassName("com.mysql.jdbc.Driver");
        poolProperties.setUsername(databaseInfo.getUserName());
        poolProperties.setPassword(databaseInfo.getPassword());
        poolProperties.setTestWhileIdle(true);
        poolProperties.setMaxActive(get(databaseInfo.getPoolMaxActive(), 10));
        poolProperties.setInitialSize(5);
        poolProperties.setMaxIdle(get(databaseInfo.getPoolMaxIdle(), 2));
        poolProperties.setMinIdle(1);
        poolProperties.setValidationQuery("SELECT 1");
        poolProperties.setTestWhileIdle(true);
        poolProperties.setValidationInterval(30000);

        DataSource dataSource = new DataSource();
        dataSource.setPoolProperties(poolProperties);

        return dataSource;
    }

    private static String buildUrl(DatabaseInfo databaseInfo) {
        return StringUtils2.append(DatabaseInfo.getDialectProtocol(databaseInfo.getDialect()),
                databaseInfo.getIp(), ":",
                databaseInfo.getPort(), "/",
                databaseInfo.getSchema(),
                "?rewriteBatchedStatements=true",
                StringUtils.isNotBlank(databaseInfo.getParams()) ? ("&" + databaseInfo.getParams()) : "");
    }

    public static void destroy() {
        dataSourceMap.values().forEach(ds -> {
            ds.close();
        });
    }

    private static Integer get(Integer source, Integer def) {
        if (source == null) {
            return def;
        } else {
            return source;
        }
    }
}
