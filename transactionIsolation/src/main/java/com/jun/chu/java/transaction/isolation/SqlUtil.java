package com.jun.chu.java.transaction.isolation;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author chujun
 * @date 2022/3/5
 */
@UtilityClass
public class SqlUtil {
    /**
     * JDBC连接的URL, 不同数据库有不同的格式
     */
    public static String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc?useSSL=false&characterEncoding=utf8";
    public static String JDBC_USER = "root";
    public static String JDBC_PASSWORD = "root";

    public static Connection getNewConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
