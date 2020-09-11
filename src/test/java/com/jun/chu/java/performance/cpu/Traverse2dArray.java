package com.jun.chu.java.performance.cpu;

import java.util.Date;

/**
 * 以不同的方式遍历2维数组,效率完全不同,会差差不多8倍
 *
 * @author chujun
 * @date 2020-09-08
 * @link https://github.com/russelltao/geektime_distrib_perf/tree/master/1-cpu_cache/traverse_2d_array
 */
public class Traverse2dArray {
    public static void main(String args[]) {
        int ch;
        int TESTN = 4096;
        boolean slowMode = false;
        for (String arg : args) {
            if ("-f".equals(arg)) {
                slowMode = false;
                break;
            } else if ("-s".equals(arg)) {
                slowMode = true;
                break;
            }
        }

        char[][] arr = new char[TESTN][TESTN];
        Date start = new Date();
        if (!slowMode) {
            for (int i = 0; i < TESTN; i++) {
                for (int j = 0; j < TESTN; j++) {
                    //arr[i][j]是连续访问的
                    arr[i][j] = 0;
                }
            }
        } else {
            for (int i = 0; i < TESTN; i++) {
                for (int j = 0; j < TESTN; j++) {
                    //arr[j][i]是不连续访问的
                    arr[j][i] = 0;
                }
            }
        }

        System.out.println(new Date().getTime() - start.getTime());
    }
}
