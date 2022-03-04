package com.jun.chu.java.transaction.isolation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author chujun
 * @date 2022/3/4
 */
public class JdbcTransaction {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws SQLException, JsonProcessingException {
        // JDBC连接的URL, 不同数据库有不同的格式:
        String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc?useSSL=false&characterEncoding=utf8";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "root";
        // 获取连接:
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            System.out.println(conn.getAutoCommit() + "," + conn.getTransactionIsolation());
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?")) {
                ps.setObject(1, 1); // 注意：索引从1开始
                ps.setObject(2, 3);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Student> students = fetchStudents(rs);
                    System.out.println(objectMapper.writeValueAsString(students));
                }
            }
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students (grade, name, gender,score) VALUES (?,?,?,?)")) {
                ps.setObject(1, 1); // 索引从1开始 grade
                ps.setObject(2, "Bob"); // name
                ps.setObject(3, 1); // gender
                ps.setObject(4, 100);//score
                int count = ps.executeUpdate(); // 1
                System.out.println("execute insert count:" + count);
            }
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE students SET name=? WHERE id=?")) {
                ps.setObject(1, "Bob"); // 注意：索引从1开始
                ps.setObject(2, 1);
                int count = ps.executeUpdate(); // 返回更新的行数
                System.out.println("execute update count:" + count);
            }
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            int maxId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id) FROM students")) {
                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                maxId = resultSet.getInt(1);
                System.out.println("maxId:" + maxId);
            }
            try (PreparedStatement deletePs = conn.prepareStatement(
                "DELETE FROM students WHERE id=?")) {
                deletePs.setObject(1, maxId); // 注意：索引从1开始
                int count = deletePs.executeUpdate(); // 删除的行数
                System.out.println("execute delete count:" + count);
            }
        }
    }

    private static List<Student> fetchStudents(final ResultSet rs) throws SQLException {
        List<Student> result = Lists.newArrayList();
        while (rs.next()) {
            long id = rs.getLong(1); // 注意：索引从1开始
            long grade = rs.getLong(2);
            String name = rs.getString(3);
            int gender = rs.getInt(4);
            result.add(Student.builder()
                .id(id).grade(grade).name(name).gender(gender)
                .build());
        }
        return result;
    }
}
