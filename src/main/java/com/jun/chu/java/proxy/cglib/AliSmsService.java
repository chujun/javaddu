package com.jun.chu.java.proxy.cglib;

/**
 * @author chujun
 * @date 2022/5/26
 */
public class AliSmsService {
    public String send(String message) {
        System.out.println("send message:" + message);
        return message;
    }

    public final String sendFinal(String message) {
        System.out.println("sendV2 message:" + message);
        return message;
    }
}
