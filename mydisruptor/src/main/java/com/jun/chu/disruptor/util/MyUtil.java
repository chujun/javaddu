package com.jun.chu.disruptor.util;

import com.jun.chu.disruptor.MySequence;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * @author chujun
 * @date 2022/2/15
 */
public final class MyUtil {

    private static final Unsafe THE_UNSAFE;

    static {
        try {
            final PrivilegedExceptionAction<Unsafe> action = () -> {
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                return (Unsafe) theUnsafe.get(null);
            };

            THE_UNSAFE = AccessController.doPrivileged(action);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    public static Unsafe getUnsafe() {
        return THE_UNSAFE;
    }

    /**
     * @param sequences
     * @return the minimum sequence found or Long.MAX_VALUE if the array is empty.
     */
    public static long getMinimumSequence(final MySequence[] sequences) {
        return getMinimumSequence(sequences, Long.MAX_VALUE);
    }

    public static long getMinimumSequence(final MySequence[] sequences, long minimum) {
        for (int i = 0, n = sequences.length; i < n; i++) {
            long value = sequences[i].get();
            minimum = Math.min(minimum, value);
        }

        return minimum;
    }

    public static int log2(int i)
    {
        int r = 0;
        while ((i >>= 1) != 0)
        {
            ++r;
        }
        return r;
    }
}
