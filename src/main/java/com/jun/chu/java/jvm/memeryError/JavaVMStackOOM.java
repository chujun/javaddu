package com.jun.chu.java.jvm.memeryError;

/**
 * 请一定不要在在64位jvm系统中操作,如一定要执行请做好备份工作，否则系统宕机自负
 * 在32位jvm虚拟机下操作
 *
 * @author chujun
 * @date 2022/1/27
 */
public class JavaVMStackOOM {
    private void dontStop() {
        while (true) {

        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            }) {
            };
            thread.start();
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM javaVMStackOOM = new JavaVMStackOOM();
        //真实执行时再开启如下语句，请先确定jvm虚拟机位数是32位还是64位，64位下谨慎备份
        javaVMStackOOM.stackLeakByThread();
    }
}
