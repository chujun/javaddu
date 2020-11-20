package com.jun.chu.java.io.bio;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author chujun
 * @date 2020-11-20
 */
@AllArgsConstructor
public class TimerServerHandler implements Runnable {
    private Socket socket;

    @Override
    public void run() {

        try (BufferedReader in =
                 new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
             PrintWriter out =
                 new PrintWriter(this.socket.getOutputStream(), true)) {
            String currentTime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if (null == body) {
                    break;
                }
                System.out.println(Thread.currentThread().toString());
                System.out.println("This time server receive order:" + body);
                currentTime = TimeServer.COMMAND_QUERY_TIME.equalsIgnoreCase(body) ?
                    new Date(System.currentTimeMillis()).toString() : "bad order";
                out.println(currentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
