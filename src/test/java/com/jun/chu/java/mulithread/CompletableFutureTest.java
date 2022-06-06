package com.jun.chu.java.mulithread;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author chujun
 * @date 2022/6/6
 */
public class CompletableFutureTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.completedFuture("hello!")
            .thenApply(s -> s + "world!");
        assertEquals("hello!world!", future.get());
        assertEquals("hello!world!", future.get());
        // 这次调用将被忽略。 TODO:cj 这是为什么，因为异步结果已经出来了？？？
        future.thenApply(s -> s + "nice!");
        assertEquals("hello!world!", future.get());
    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
        CompletableFuture.completedFuture("hello!")
            .thenApply(s -> s + "world!").thenApply(s -> s + "nice!").thenAccept(System.out::println);//hello!world!nice!

        CompletableFuture.completedFuture("hello!")
            .thenApply(s -> s + "world!").thenApply(s -> s + "nice!").thenRun(() -> System.out.println("hello!"));//hello!

        CompletableFuture<String> future = CompletableFuture.completedFuture("hello!")
            .thenApply(s -> s + "world!").thenApply(s -> s + "nice!");
        assertEquals("hello!world!nice!", future.get());
    }

    @Test
    public void testWhenComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello!")
            .whenComplete((res, ex) -> {
                // res 代表返回的结果
                // ex 的类型为 Throwable ，代表抛出的异常
                System.out.println(res);
                // 这里没有抛出异常所有为 null
                assertNull(ex);
            });
        assertEquals("hello!", future.get());
    }

    @Test
    public void testExceptionHandle() throws ExecutionException, InterruptedException {
        //第一种异常处理方法handle()方法方式
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("Computation error!");
            }
            return "hello!";
        }).handle((res, ex) -> {
            // res 代表返回的结果
            // ex 的类型为 Throwable ，代表抛出的异常
            System.out.println(ex.toString());
            return res != null ? res : "world!";
        });
        assertEquals("world!", future.get());

        //第二种处理异常方式exceptionally()方法
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("Computation error!");
            }
            return "hello!";
        }).exceptionally(ex -> {
            System.out.println(ex.toString());// CompletionException
            return "world!";
        });
        assertEquals("world!", future2.get());

        //第三种处理异常方式completeExceptionally()方式
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new IllegalArgumentException("Computation error:IllegalArgumentException");
            }
            return "hello!";
        });
        //抛出指定的异常，一般都是将捕获的异常吐出去
        completableFuture.completeExceptionally(new RuntimeException("Calculation failed!"));
        try {
            completableFuture.get(); // ExecutionException
            Assert.fail();
        } catch (Exception e) {
            System.out.println(e.toString());
            Assert.assertEquals("java.lang.RuntimeException: Calculation failed!", e.getMessage());
        }

    }

    @Test
    public void test5() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello!")
            .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + "world!"));
        assertEquals("hello!world!", future.get());

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "hello!")
            //并行没有先后顺序
            .thenCombine(CompletableFuture.supplyAsync(() -> "world!"), (s1, s2) -> s1 + s2)
            //存在先后顺序
            .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + "nice!"));
        assertEquals("hello!world!nice!", completableFuture.get());
    }
}
