package com.jun.chu.java.jvm.memeryError;

/**
 * -VM:
 * -Xss200k
 * 设置太小，各个操作系统有下限要求
 * macos下报错
 * The stack size specified is too small, Specify at least 160k
 * Error: Could not create the Java Virtual Machine.
 * Error: A fatal exception has occurred. Program will exit.
 *
 * @author chujun
 * @date 2022/1/26
 */
public class JavaVMStackStackOverFlowError {
    private int stockLength = 1;

    /**
     * 实验结果统计 macos jdk8
     * ------------------------------------------------------------------------
     * 1.-Xss200k
     * 错误信息
     * stock length:1239
     * Exception in thread "main" java.lang.StackOverflowError
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:18)
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:19)
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:19)
     * <p>
     * ------------------------------------------------------------------------
     * 2.-Xss400k
     * stock length:3566
     * Exception in thread "main" java.lang.StackOverflowError
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:19)
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:20)
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:20)
     * ------------------------------------------------------------------------
     */
    public void stackLeak() {
        stockLength++;
        stackLeak();
    }

    /**
     * 实验结果统计 macos jdk8
     * ------------------------------------------------------------------------
     * 1.-Xss200k
     * stock length:1232
     * Exception in thread "main" java.lang.StackOverflowError
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:39)
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:40)
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:40)
     * ------------------------------------------------------------------------
     * 2.-Xss200k
     * stock length:3559
     * Exception in thread "main" java.lang.StackOverflowError
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:40)
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:41)
     * at com.jun.chu.java.jvm.memeryError.JavaVMStockStackOverFlowError.stackLeak(JavaVMStockStackOverFlowError.java:41)
     */
    public void stockLeakForMuchLocalVariables() {
        long unused1, unused2, unused3, unused4, unused5, unused6, unused7, unused8, unused9, unused10,
            unused11, unused12, unused13, unused14, unused15, unused16, unused17, unused18, unused19, unused20,
            unused21, unused22, unused23, unused24, unused25, unused26, unused27, unused28, unused29, unused30,
            unused31, unused32, unused33, unused34, unused35, unused36, unused37, unused38, unused39, unused40;
        stockLength++;
        stackLeak();
        unused1 = unused2 = unused3 = unused4 = unused5 = 5;
        unused6 = unused7 = unused8 = unused9 = unused10 = 10;
        unused11 = unused12 = unused13 = unused14 = unused15 = 15;
        unused16 = unused17 = unused18 = unused19 = unused20 = 110;
        unused21 = unused22 = unused23 = unused24 = unused25 = 115;
        unused26 = unused27 = unused28 = unused29 = unused30 = 1110;
        unused31 = unused32 = unused33 = unused34 = unused35 = 115;
        unused36 = unused37 = unused38 = unused39 = unused40 = 210;
    }


    public static void main(String[] args) {
        JavaVMStackStackOverFlowError oom = new JavaVMStackStackOverFlowError();
        try {
            //oom.stackLeak();
            oom.stockLeakForMuchLocalVariables();
        } catch (Error e) {
            System.out.println(" stock length:" + oom.stockLength);
            throw e;
        }
    }

}

