package com.jun.chu.java.math;

/**
 * @author chujun
 * @date 2019-09-11 17:26
 */
public class Random {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(generateRandomInteger(100, 600));
        }
    }

    private static long generateRandomInteger(int min, int max) {
        double random = Math.random();
        return (long) (random * (max - min) + min);
    }
}
