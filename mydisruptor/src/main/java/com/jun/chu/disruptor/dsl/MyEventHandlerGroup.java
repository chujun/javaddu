package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyEventProcessor;
import com.jun.chu.disruptor.MySequence;
import com.jun.chu.disruptor.MySequenceBarrier;
import com.jun.chu.disruptor.MyWorkHandler;

import java.util.Arrays;

/**
 * @author chujun
 * @date 2022/2/19
 */
public class MyEventHandlerGroup<T> {
    private final MyDisruptor<T> disruptor;
    private final MyConsumerRepository<T> consumerRepository;
    private final MySequence[] sequences;

    MyEventHandlerGroup(
        final MyDisruptor<T> disruptor,
        final MyConsumerRepository<T> consumerRepository,
        final MySequence[] sequences) {
        this.disruptor = disruptor;
        this.consumerRepository = consumerRepository;
        this.sequences = Arrays.copyOf(sequences, sequences.length);
    }

    /**
     * this的sequences和otherHandlerGroup和sequences组合起来，添加在数组后面
     */
    public MyEventHandlerGroup<T> and(final MyEventHandlerGroup<T> otherHandlerGroup) {
        final MySequence[] combinedSequences = new MySequence[this.sequences.length + otherHandlerGroup.sequences.length];
        System.arraycopy(this.sequences, 0, combinedSequences, 0, this.sequences.length);
        System.arraycopy(
            otherHandlerGroup.sequences, 0,
            combinedSequences, this.sequences.length, otherHandlerGroup.sequences.length);
        return new MyEventHandlerGroup<>(disruptor, consumerRepository, combinedSequences);
    }

    /**
     * this的sequences和processors数组的sequences组合在一起，添加在数组后面
     */
    public MyEventHandlerGroup<T> and(final MyEventProcessor... processors) {
        MySequence[] combinedSequences = new MySequence[sequences.length + processors.length];

        for (int i = 0; i < processors.length; i++) {
            consumerRepository.add(processors[i]);
            combinedSequences[i] = processors[i].getSequence();
        }
        System.arraycopy(sequences, 0, combinedSequences, processors.length, sequences.length);

        return new MyEventHandlerGroup<>(disruptor, consumerRepository, combinedSequences);
    }

    @SafeVarargs
    public final MyEventHandlerGroup<T> then(final MyEventHandler<? super T>... handlers) {
        return handleEventsWith(handlers);
    }

    @SafeVarargs
    public final MyEventHandlerGroup<T> then(final MyEventProcessorFactory<T>... eventProcessorFactories) {
        return handleEventsWith(eventProcessorFactories);
    }


    @SafeVarargs
    public final MyEventHandlerGroup<T> thenHandleEventsWithWorkerPool(final MyWorkHandler<? super T>... handlers)
    {
        return handleEventsWithWorkerPool(handlers);
    }


    @SafeVarargs
    public final MyEventHandlerGroup<T> handleEventsWith(final MyEventHandler<? super T>... handlers) {
        return disruptor.createEventProcessors(sequences, handlers);
    }


    @SafeVarargs
    public final MyEventHandlerGroup<T> handleEventsWith(final MyEventProcessorFactory<T>... eventProcessorFactories) {
        return disruptor.createEventProcessors(sequences, eventProcessorFactories);
    }

    @SafeVarargs
    public final MyEventHandlerGroup<T> handleEventsWithWorkerPool(final MyWorkHandler<? super T>... handlers)
    {
        return disruptor.createWorkerPool(sequences, handlers);
    }

    public MySequenceBarrier asSequenceBarrier()
    {
        return disruptor.getRingBuffer().newBarrier(sequences);
    }


}
