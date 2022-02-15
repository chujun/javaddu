package com.jun.chu.disruptor;

import com.jun.chu.disruptor.util.MyUtil;
import sun.misc.Unsafe;

/**
 * @author chujun
 * @date 2022/2/15
 */
class MyLhsPadding {
    //7*8+ 8(value long)=64位
    protected long p1, p2, p3, p4, p5, p6, p7;
}

class MyValue extends MyLhsPadding {
    /**
     * 前后正好都填充至64位，缓存行
     */
    protected volatile long value;
}

class MyRhsPadding extends MyValue {
    protected long p9, p10, p11, p12, p13, p14, p15;
}

/**
 * 支持并发操作
 * 性能优化：处理伪共享通过填充空白方式
 */
public class MySequence extends MyRhsPadding {
    static final long INITIAL_VALUE = -1L;
    private static final Unsafe UNSAFE;
    private static final long VALUE_OFFSET;

    static {
        UNSAFE = MyUtil.getUnsafe();
        try {
            VALUE_OFFSET = UNSAFE.objectFieldOffset(MyValue.class.getDeclaredField("value"));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MySequence() {
        this(INITIAL_VALUE);
    }

    public MySequence(final long initialValue) {
        UNSAFE.putOrderedLong(this, VALUE_OFFSET, initialValue);
    }

    /**
     * volatile读，value用volatile修饰
     */
    public long get() {
        return value;
    }

    /**
     * 完成sequence的顺序写。目的是Store/Store barrier 屏障,在这次写write和之前的store
     */
    public void set(final long value) {
        UNSAFE.putOrderedLong(this, VALUE_OFFSET, value);
    }

    /**
     * 完成sequence的volatile写。目的是Store/Store屏障(在这次写和之前的store)和Store/Load屏障(这次写和任意接下来的volatile读)
     */
    public void setVolatile(final long value) {
        UNSAFE.putLongVolatile(this, VALUE_OFFSET, value);
    }

    /**
     * CAS 操作
     */
    public boolean compareAndSet(final long expectedValue, final long newValue) {
        return UNSAFE.compareAndSwapLong(this, VALUE_OFFSET, expectedValue, newValue);
    }

    /**
     * 原子操作
     * 原子递增
     */
    public long incrementAndGet() {
        return addAndGet(1L);
    }

    /**
     * 原子操作，基于CAS
     */
    public long addAndGet(final long increment) {
        long currentValue;
        long newValue;

        do {
            currentValue = get();
            newValue = currentValue + increment;
        }
        while (!compareAndSet(currentValue, newValue));

        return newValue;
    }

    @Override
    public String toString() {
        return Long.toString(get());
    }
}
