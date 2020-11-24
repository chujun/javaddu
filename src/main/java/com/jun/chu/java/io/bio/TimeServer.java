package com.jun.chu.java.io.bio;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * adjust from <<netty权威指南>>第二版 李林锋著
 *
 * @author chujun
 * @date 2020-11-20
 */
public class TimeServer {
    public static final Integer DEFAULT_PORT = 18080;
    public static final String COMMAND_QUERY_TIME = "query time order";
    public static final String COMMAND_BYE = "bye";

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (ArrayUtils.isNotEmpty(args)) {
            port = NumberUtils.toInt(args[0], port);
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("This time server is start in port:" + port);
            Socket socket = null;
            while (true) {
                //用idea最快捷或者jvisualvm/jstack命令可以dump线程池下来，线程阻塞在accept runnable状态
                socket = server.accept();
                new Thread(new TimerServerHandler(socket)).start();
            }
        } finally {
            if (Objects.nonNull(server)) {
                System.out.println("time server close");
                server.close();
                server = null;
            }
        }

    }
}
