package com.jun.chu.java.io.bio;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

        try (
            InputStream inputStream = socket.getInputStream();
            BufferedReader in =
                new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter out =
                new PrintWriter(this.socket.getOutputStream(), true)) {
            String currentTime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    Thread.sleep(50);
                }
                if (TimeServer.COMMAND_BYE.equals(body)) {
                    //需要有关闭协议,还需要超时协议来保护服务器，不然服务器线程一直死不掉
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
