package com.jun.chu.java.mulitread;

/**
 * Created by chujun on 17/2/10.
 * 参考资料:java面试宝典2016年版
 * 1.本demo主要说明java多线程中sleep方法和wait方法的区别.
 * sleep:释放cpu资源,不释放同步锁资源
 * wait:释放cpu资源,也释放同步锁资源
 * notify:不释放cpu资源,不释放同步锁资源
 * <p>
 * 2.以及wait方法和notify方法的简单使用方法。
 * wait:调用同步块内监视器的wait方法释放锁资源
 * notify:调用监视器的notify方法唤醒调用过该监视器的wait方法的若干线程中的一个线程参与该锁的竞争
 * notifyAll:调用监视器的notify方法唤醒所有调用过该监视器的wait方法的线程们参与该锁的竞争
 */
public class SleepAndWaitCompare extends BaseThread {
    public static void main(String[] args) {
        new Thread(new Thread1()).start();
        try {
            echo("让" + Thread1.class.getSimpleName() + "先飞起来");
            comment("主线程睡眠确保" + Thread1.class.getSimpleName() + "先行执行");
            Thread.sleep(base_time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Thread2()).start();
        while (true) {
            try {
                Thread.sleep(base_time / 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            echo("我在工作");
        }
    }
}

class BaseThread {
    public static final int base_time = 1000;

    public static void echo(String str) {
        System.out.println(getThreadName() + ":" + str);
    }

    public static void comment(String str) {
        echo("-------------" + str);
    }

    private static String getThreadName() {
        return Thread.currentThread().getName();
    }
}

class Thread1 extends BaseThread implements Runnable {

    public void run() {
        echo("开始执行");
        comment("这里必须使用SleepAndWaitCompare.class作为两个线程的同一个监视器,若使用this则是两个不同的同步锁");
        synchronized (SleepAndWaitCompare.class) {
            echo("进入同步块");
            comment("释放锁的第二种方式:在synchronized块内调用监视器对象的wait方法释放同步锁,这里监视器是指" + SleepAndWaitCompare.class.getSimpleName());
            try {
                echo("调用监视器" + SleepAndWaitCompare.class.getSimpleName() + "的wait方法,等待监视器的notify方法和notifyAll方法的呼唤");
                SleepAndWaitCompare.class.wait();
                echo("我终于得到同步锁了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        echo("离开同步块");
        comment("释放锁的第一种方式:离开监视器范围,也就是离开同步块代码");
        echo("我执行结束了");
    }
}

class Thread2 extends BaseThread implements Runnable {

    public void run() {
        echo("开始执行");
        synchronized (SleepAndWaitCompare.class) {
            echo("进入同步块");
            SleepAndWaitCompare.class.notifyAll();
            comment("调用监视器的notify方法,会唤醒调用过监视器的wait方法的中的一个线程参与到锁的竞争(注意不是马上获得锁),但并没有释放当前线程的同步锁");
            try {
                echo("开始睡眠" + base_time + "ms");
                comment("调用sleep方法会出让cpu资源,但是不会释放同步锁");
                Thread.sleep(base_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            echo("睡醒了,我仍然拥把这同步锁哦");
            comment("调用监视器的notify方法本身不释放同步锁,同步块内notify方法后面的代码同样会继续执行");
        }
        echo("离开同步块");
        echo("我执行结束了");
    }
}
///Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/bin/java -Didea.launcher.port=7532 "-Didea.launcher.bin.path=/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/lib/tools.jar:/Users/chujun/Documents/idea_workspace/my/javaddu/target/classes:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar" com.intellij.rt.execution.application.AppMain com.jun.chu.java.mulitread.SleepAndWaitCompare
//        main:让Thread1先飞起来
//        Thread-0:开始执行
//        Thread-0:-------------这里必须使用SleepAndWaitCompare.class作为两个线程的同一个监视器,若使用this则是两个不同的同步锁
//        Thread-0:进入同步块
//        main:-------------主线程睡眠确保Thread1先行执行
//        Thread-0:-------------释放锁的第二种方式:在synchronized块内调用监视器对象的wait方法释放同步锁,这里监视器是指SleepAndWaitCompare
//        Thread-0:调用监视器SleepAndWaitCompare的wait方法,等待监视器的notify方法和notifyAll方法的呼唤
//        Thread-1:开始执行
//        Thread-1:进入同步块
//        Thread-1:-------------调用监视器的notify方法,会唤醒调用过监视器的wait方法的中的一个线程参与到锁的竞争(注意不是马上获得锁),但并没有释放当前线程的同步锁
//        Thread-1:开始睡眠1000ms
//        Thread-1:-------------调用sleep方法会出让cpu资源,但是不会释放同步锁
//        main:我在工作
//        main:我在工作
//        Thread-1:我仍然拥把这同步锁哦
//        Thread-1:-------------调用监视器的notify方法本身不释放同步锁,同步块内notify方法后面的代码同样会继续执行
//        Thread-1:离开同步块
//        Thread-1:我执行结束了
//        Thread-0:我终于得到同步锁了
//        Thread-0:离开同步块
//        Thread-0:-------------释放锁的第一种方式:离开监视器范围,也就是离开同步块代码
//        Thread-0:我执行结束了
//        main:我在工作
//        main:我在工作
//        main:我在工作
//        main:我在工作
//        main:我在工作
//        main:我在工作
//        main:我在工作
//        main:我在工作
//
//        Process finished with exit code 130
