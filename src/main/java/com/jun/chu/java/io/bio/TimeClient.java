package com.jun.chu.java.io.bio;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

/**
 * @author chujun
 * @date 2020-11-20
 */
public class TimeClient {
    public static void main(String[] args) throws IOException {
        int port = TimeServer.DEFAULT_PORT;
        if (Objects.nonNull(args) && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        try (Socket socket = new Socket("localhost", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(TimeServer.COMMAND_QUERY_TIME + 1);
            System.out.println("send order 2 server succeed");
            String resp = in.readLine();
            System.out.println("now is " + resp);
        }
    }
}
