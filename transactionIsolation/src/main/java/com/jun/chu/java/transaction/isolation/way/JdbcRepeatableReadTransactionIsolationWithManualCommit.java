package com.jun.chu.java.transaction.isolation.way;

import com.jun.chu.java.transaction.isolation.Assert;
import com.jun.chu.java.transaction.isolation.JsonUtil;
import com.jun.chu.java.transaction.isolation.SqlUtil;
import com.jun.chu.java.transaction.isolation.Student;
import com.jun.chu.java.transaction.isolation.StudentHelper;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jdbc mysql默认隔离级别可重复读+事务手动提交
 * <p>
 * 多线程时间执行顺序
 * A线程查询id为1的学生信息
 * B线程更新id为1的学生信息
 * A线程再查询id为1的学生信息
 *
 * @author chujun
 * @date 2022/3/5
 */
public class JdbcRepeatableReadTransactionIsolationWithManualCommit {
    private static final Lock LOCK = new ReentrantLock();
    /**
     * 手动提交事务
     */
    private static final Condition CONDITION_MANUAL_COMMIT = LOCK.newCondition();
    private static final String RANDOM_INT = "" + new Random().nextInt();

    public static void main(String[] args) {
        new Thread(new RunnableB()).start();
        new Thread(new RunnableA()).start();
    }

    public static class RunnableA implements Runnable {

        /**
         * 事务手动提交+mysql默认隔离级别可重复读
         */
        @Override
        public void run() {
            List<Student> result;
            try (Connection conn = SqlUtil.getNewConnection()) {
                conn.setAutoCommit(false);
                System.out.println("A autoCommit:" + conn.getAutoCommit() + ",TransactionIsolation:" + conn.getTransactionIsolation());
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE id=? ")) {
                    ps.setObject(1, 1);
                    try (ResultSet rs = ps.executeQuery()) {
                        result = StudentHelper.fetchStudents(rs);
                        System.out.println("A first query:" + JsonUtil.toJson(result));
                    }
                }
                System.out.println("A start to await");
                LOCK.lock();
                try {
                    CONDITION_MANUAL_COMMIT.signalAll();
                    CONDITION_MANUAL_COMMIT.await();
                } finally {
                    LOCK.unlock();
                }
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE id=?")) {
                    ps.setObject(1, 1);
                    try (ResultSet rs = ps.executeQuery()) {
                        List<Student> students = StudentHelper.fetchStudents(rs);
                        //因为事务手动提交了+事务隔离级别为重复读,所以不能读取到另一个事务提交的数据
                        System.out.println("A second query:" + JsonUtil.toJson(students));
                        Assert.isTrue(Objects.equals(result, students), new RuntimeException("not true"));
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
            LOCK.lock();
            try {
                CONDITION_MANUAL_COMMIT.await();
            } finally {
                LOCK.unlock();
            }

            try (Connection conn = SqlUtil.getNewConnection()) {
                try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE students SET name=? WHERE id=?")) {
                    System.out.println("randomInt:" + RANDOM_INT);
                    ps.setObject(1, RANDOM_INT);
                    ps.setObject(2, 1);
                    // 返回更新的行数
                    int count = ps.executeUpdate();
                    System.out.println("B execute update count:" + count);
                }

                LOCK.lock();
                try {
                    CONDITION_MANUAL_COMMIT.signalAll();
                } finally {
                    LOCK.unlock();
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
                throw new RuntimeException(throwable);
            }
        }
    }
}
