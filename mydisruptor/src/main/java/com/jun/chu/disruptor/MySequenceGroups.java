package com.jun.chu.disruptor;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import static java.util.Arrays.copyOf;

/**
 * @author chujun
 * @date 2022/2/17
 */
public class MySequenceGroups {
    /**
     * 1.更新holder对象的updater方法的sequenced值
     * 2.更新入参sequencesToAdd数组的每个sequence的value值
     */
    static <T> void addSequences(
        final T holder,
        final AtomicReferenceFieldUpdater<T, MySequence[]> updater,
        final MyCursored cursor,
        final MySequence... sequencesToAdd)
    {
        long cursorSequence;
        MySequence[] updatedSequences;
        MySequence[] currentSequences;

        do
        {
            currentSequences = updater.get(holder);
            updatedSequences = copyOf(currentSequences, currentSequences.length + sequencesToAdd.length);
            cursorSequence = cursor.getCursor();

            int index = currentSequences.length;
            for (MySequence sequence : sequencesToAdd)
            {
                sequence.set(cursorSequence);
                updatedSequences[index++] = sequence;
            }
        }
        while (!updater.compareAndSet(holder, currentSequences, updatedSequences));
        //TODO为什么又重新设置一遍，并发访问原因？
        cursorSequence = cursor.getCursor();
        for (MySequence sequence : sequencesToAdd)
        {
            sequence.set(cursorSequence);
        }
    }

    static <T> boolean removeSequence(
        final T holder,
        final AtomicReferenceFieldUpdater<T, MySequence[]> sequenceUpdater,
        final MySequence sequence)
    {
        int numToRemove;
        MySequence[] oldSequences;
        MySequence[] newSequences;

        do
        {
            oldSequences = sequenceUpdater.get(holder);

            numToRemove = countMatching(oldSequences, sequence);

            if (0 == numToRemove)
            {
                break;
            }

            final int oldSize = oldSequences.length;
            newSequences = new MySequence[oldSize - numToRemove];

            for (int i = 0, pos = 0; i < oldSize; i++)
            {
                final MySequence testSequence = oldSequences[i];
                if (sequence != testSequence)
                {
                    newSequences[pos++] = testSequence;
                }
            }
        }
        while (!sequenceUpdater.compareAndSet(holder, oldSequences, newSequences));

        return numToRemove != 0;
    }

    private static <T> int countMatching(T[] values, final T toMatch)
    {
        int numToRemove = 0;
        for (T value : values)
        {
            if (value == toMatch) // Specifically uses identity
            {
                numToRemove++;
            }
        }
        return numToRemove;
    }
}
