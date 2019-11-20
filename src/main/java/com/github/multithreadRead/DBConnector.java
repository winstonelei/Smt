package com.github.multithreadRead;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DB 连接器，维护连接
 * @author winstone
 * @date 2019/4/28 下午8:08
 */
public class DBConnector {

    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String username = "root";
    private static String password = "";
    private static String url = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=UTF-8";

    private Connection connection;

    public Connection openConnection() {

        if (connection != null) {
            return connection;
        }

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void closeConnection() throws Exception{
        if (this.connection != null) {
            this.connection.close();
        }
    }



    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
