package com.jun.chu.java.jvm.finalize;

/**
 * 一次对象自我拯救的示例，通过@Override finalize
 * 不过Finalize方法已经不推荐使用了，在jdk后续版本标记成待废弃方法了，建议大家忘记这个方法
 * 对象可以在finalize方法自救一次，但最多也只有一次，因为finalize方法最多只能执行一次
 *
 * @author chujun
 * @date 2022/2/7
 */
public class FinalizeEscapeGC {
    private static FinalizeEscapeGC SAVE_HOOK = null;

    public void isAlive() {
        System.out.println("yes,I am still alive:");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed");
        FinalizeEscapeGC.SAVE_HOOK = this;
    }

    /**
     * 输出结果如下:
     * finalize method executed
     * yes,I am still alive:
     * no,I am dead!
     */
    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();

        //第一次对象自救自己一次
        SAVE_HOOK = null;
        //Finalizer线程去执行它们的finalize()方法，Finalizer线程优先级极低
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no,I am dead!");
        }

        //完全一模一样的代码，但这次自救失败了，因为一个对象的finalize最多只能执行一次
        SAVE_HOOK = null;
        //Finalizer线程去执行它们的finalize()方法，Finalizer线程优先级极低
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no,I am dead!");
        }
    }

}
