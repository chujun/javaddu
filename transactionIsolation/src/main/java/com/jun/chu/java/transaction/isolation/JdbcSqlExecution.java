package com.jun.chu.java.transaction.isolation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author chujun
 * @date 2022/3/4
 */
public class JdbcSqlExecution {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = SqlUtil.getNewConnection()) {
            System.out.println(conn.getAutoCommit() + "," + conn.getTransactionIsolation());
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?")) {
                // 注意：索引从1开始
                ps.setObject(1, 1);
                ps.setObject(2, 3);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Student> students = StudentHelper.fetchStudents(rs);
                    System.out.println(JsonUtil.toJson(students));
                }
            }
        }

        try (Connection conn = SqlUtil.getNewConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students (grade, name, gender,score) VALUES (?,?,?,?)")) {
                // 索引从1开始 grade
                ps.setObject(1, 1);
                // name
                ps.setObject(2, "Bob");
                // gender
                ps.setObject(3, 1);
                //score
                ps.setObject(4, 100);
                // 1
                int count = ps.executeUpdate();
                System.out.println("execute insert count:" + count);
            }
        }

        try (Connection conn = SqlUtil.getNewConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE students SET name=? WHERE id=?")) {
                // 注意：索引从1开始
                ps.setObject(1, "Bob");
                ps.setObject(2, 1);
                // 返回更新的行数
                int count = ps.executeUpdate();
                System.out.println("execute update count:" + count);
            }
        }

        try (Connection conn = SqlUtil.getNewConnection()) {
            int maxId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id) FROM students")) {
                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                maxId = resultSet.getInt(1);
                System.out.println("maxId:" + maxId);
            }
            try (PreparedStatement deletePs = conn.prepareStatement(
                "DELETE FROM students WHERE id=?")) {
                // 注意：索引从1开始
                deletePs.setObject(1, maxId);
                // 删除的行数
                int count = deletePs.executeUpdate();
                System.out.println("execute delete count:" + count);
            }
        }
    }


}
