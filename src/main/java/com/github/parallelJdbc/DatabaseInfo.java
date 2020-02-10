package com.github.parallelJdbc;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 */
@SuppressWarnings("unchecked")
public class DatabaseInfo {

    private static Map<String, String> protocolMap = new HashMap();

    static {
        protocolMap.put("mysql", "jdbc:mysql://");
    }

    private String dialect;

    private String ip;

    private int port;

    private String schema;

    private String params;

    private String userName;

    private String password;

    private Properties properties = new Properties();

    private Integer poolMaxActive;

    private Integer poolMaxIdle;

    public static String getDialectProtocol(String dialect) {
        String result = protocolMap.get(dialect.toLowerCase());
        if (result == null || result.trim().length() == 0) {
            throw new IllegalArgumentException(String.format("不支持这种数据库dialect=[]", dialect));
        }

        return result;
    }

    public DatabaseInfo() {
    }

    public DatabaseInfo(String dialect, String ip, int port, String schema, String userName, String password) {
        this.dialect = dialect;
        this.ip = ip;
        this.port = port;
        this.schema = schema;
        this.userName = userName;
        this.password = password;
    }

    public DatabaseInfo(String dialect, String ip, int port, String schema, String params, String userName, String password, Properties properties) {
        this.dialect = dialect;
        this.ip = ip;
        this.port = port;
        this.schema = schema;
        this.params = params;
        this.userName = userName;
        this.password = password;
        this.properties = properties;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Integer getPoolMaxActive() {
        return poolMaxActive;
    }

    public void setPoolMaxActive(Integer poolMaxActive) {
        this.poolMaxActive = poolMaxActive;
    }

    public Integer getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(Integer poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
