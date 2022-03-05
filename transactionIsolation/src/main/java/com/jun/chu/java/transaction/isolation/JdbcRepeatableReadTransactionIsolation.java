package com.jun.chu.java.transaction.isolation;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 可重复读
 *
 * @author chujun
 * @date 2022/3/5
 */
public class JdbcRepeatableReadTransactionIsolation {
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);
    private static final CountDownLatch STARTED = new CountDownLatch(1);
    private static final Integer RANDOM_INT = new Random().nextInt();

    public static void main(String[] args) {
        new Thread(new RunnableB()).start();
        new Thread(new RunnableA()).start();
    }

    public static class RunnableA implements Runnable {

        /**
         * 事务自动提交+mysql隔离级别可重复读
         */
        @Override
        public void run() {
            try (Connection conn = SqlUtil.getNewConnection()) {
                System.out.println("autoCommit:" + conn.getAutoCommit() + ",TransactionIsolation:" + conn.getTransactionIsolation());
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE id=? ")) {
                    ps.setObject(1, 1);
                    try (ResultSet rs = ps.executeQuery()) {
                        List<Student> students = StudentHelper.fetchStudents(rs);
                        System.out.println("A first query:" + JsonUtil.toJson(students));
                    }
                }
                STARTED.countDown();
                COUNT_DOWN_LATCH.await();
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE id=?")) {
                    ps.setObject(1, 1);
                    try (ResultSet rs = ps.executeQuery()) {
                        List<Student> students = StudentHelper.fetchStudents(rs);
                        //因为事务自动提交了，所以能读取到另一个事务提交的数据
                        System.out.println("A second query:" + JsonUtil.toJson(students));
                    }
                }
            } catch (SQLException | InterruptedException throwable) {
                throwable.printStackTrace();
                throw new RuntimeException(throwable);
            }
        }
    }

    public static class RunnableB implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            System.out.println("B start to await");
            STARTED.await();
            try (Connection conn = SqlUtil.getNewConnection()) {
                try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE students SET name=? WHERE id=?")) {
                    System.out.println("randomInt:" + RANDOM_INT);
                    ps.setObject(1, "" + RANDOM_INT);
                    ps.setObject(2, 1);
                    // 返回更新的行数
                    int count = ps.executeUpdate();
                    System.out.println("B execute update count:" + count);
                }
                COUNT_DOWN_LATCH.countDown();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
                throw new RuntimeException(throwable);
            }
        }
    }
}
