package com.jun.chu.java.io.bio;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO:cj 考虑下如何压测,看看这个TimeServer能处理多少个请求,qps是多少,怎么压测
 *
 * @author chujun
 * @date 2020-11-20
 */
public class TimePressureClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = TimeServer.DEFAULT_PORT;
        if (Objects.nonNull(args) && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        ThreadPoolExecutor threadPoolExecutor = buildThreadPoolExecutor();
        final int finalPort = port;
        for (int i = 0; i < 200; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    callServerTime(finalPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        threadPoolExecutor.shutdown();
        while (!threadPoolExecutor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
        }
        System.out.println("all executed");
    }

    private static ThreadPoolExecutor buildThreadPoolExecutor() {
        BlockingQueue bq = new ArrayBlockingQueue(100000, false);
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setDaemon(false)
            .setNameFormat("timeClientHandler")
            .setUncaughtExceptionHandler((Thread t, Throwable e) -> {
                System.out.println(t.getName() + "uncaught exception " + e.getStackTrace());
            })
            .build();

        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
        return new ThreadPoolExecutor(100, 120,
            1, TimeUnit.MINUTES, bq, threadFactory, rejectedExecutionHandler);
    }

    private static void callServerTime(final int port) throws IOException {
        try (Socket socket = new Socket("localhost", port);
             InputStream inputStream = socket.getInputStream();
             BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(TimeServer.COMMAND_QUERY_TIME + 1);
            System.out.println("send order 2 server succeed");
            if (inputStream.available() > 0) {
                String resp = in.readLine();
                System.out.println("now is " + resp);
            } else {
                System.out.println("now is not read");
            }
            out.println(TimeServer.COMMAND_BYE);
        }
    }
}
