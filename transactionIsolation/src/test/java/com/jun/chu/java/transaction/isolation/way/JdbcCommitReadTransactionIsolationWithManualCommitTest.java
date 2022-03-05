package com.jun.chu.java.transaction.isolation.way;

import com.jun.chu.java.transaction.isolation.JsonUtil;
import com.jun.chu.java.transaction.isolation.SqlUtil;
import com.jun.chu.java.transaction.isolation.Student;
import com.jun.chu.java.transaction.isolation.StudentHelper;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jdbc 提交读+事务手动提交
 * <p>
 * 多线程时间执行顺序
 * A线程查询id为1的学生信息
 * B线程更新id为1的学生信息
 * A线程再查询id为1的学生信息
 *
 * @author chujun
 * @date 2022/3/5
 */
public class JdbcCommitReadTransactionIsolationWithManualCommitTest {
    private static final Lock LOCK = new ReentrantLock();
    /**
     * 手动提交事务
     */
    private static final Condition CONDITION_MANUAL_COMMIT = LOCK.newCondition();
    private static final String RANDOM_INT = "" + new Random().nextInt();

    @Test
    public void test() {
        new Thread(new RunnableB()).start();
        new Thread(new RunnableA()).start();
    }

    public static class RunnableA implements Runnable {

        @Override
        public void run() {
            List<Student> result;
            try (Connection conn = SqlUtil.getNewConnection()) {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
                        //因为事务隔离级别是提交读，无论事务是否提交，都能读取到另一个事务提交的数据
                        result.get(0).setName(RANDOM_INT);
                        System.out.println("A second query:" + JsonUtil.toJson(students));
                        Assert.assertEquals(result, students);
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
