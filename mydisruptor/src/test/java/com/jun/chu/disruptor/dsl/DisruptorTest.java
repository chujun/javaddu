package com.jun.chu.disruptor.dsl;

import com.jun.chu.disruptor.MyBatchEventProcessor;
import com.jun.chu.disruptor.MyBlockingWaitStrategy;
import com.jun.chu.disruptor.MyEventHandler;
import com.jun.chu.disruptor.MyEventTranslator;
import com.jun.chu.disruptor.MyEventTranslatorOneArg;
import com.jun.chu.disruptor.MyRingBuffer;
import com.jun.chu.disruptor.dsl.stubs.DelayedEventHandler;
import com.jun.chu.disruptor.dsl.stubs.SleepingEventHandler;
import com.jun.chu.disruptor.dsl.stubs.StubThreadFactory;
import com.jun.chu.disruptor.dsl.stubs.TestWorkHandler;
import com.jun.chu.disruptor.supports.TestEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author chujun
 * @date 2022/2/19
 */
public class DisruptorTest {
    private static final int TIMEOUT_IN_SECONDS = 2;

    private final Collection<DelayedEventHandler> delayedEventHandlers = new ArrayList<DelayedEventHandler>();
    private final Collection<TestWorkHandler> testWorkHandlers = new ArrayList<TestWorkHandler>();
    private MyDisruptor<TestEvent> disruptor;
    private StubThreadFactory executor;
    private MyRingBuffer<TestEvent> ringBuffer;
    private TestEvent lastPublishedEvent;

    @Before
    public void setUp() throws Exception {
        createDisruptor();
    }

    @After
    public void tearDown() throws Exception {
        for (DelayedEventHandler delayedEventHandler : delayedEventHandlers) {
            delayedEventHandler.stopWaiting();
        }
        for (TestWorkHandler testWorkHandler : testWorkHandlers) {
            testWorkHandler.stopWaiting();
        }

        disruptor.halt();
        executor.joinAllThreads();
    }

    @Test
    public void shouldProcessMessagesPublishedBeforeStartIsCalled() throws Exception
    {
        final CountDownLatch eventCounter = new CountDownLatch(2);
        disruptor.handleEventsWith(new MyEventHandler<TestEvent>()
        {
            @Override
            public void onEvent(final TestEvent event, final long sequence, final boolean endOfBatch) throws Exception
            {
                eventCounter.countDown();
            }
        });

        disruptor.publishEvent(
            new MyEventTranslator<TestEvent>()
            {
                @Override
                public void translateTo(final TestEvent event, final long sequence)
                {
                    lastPublishedEvent = event;
                }
            });

        disruptor.start();

        disruptor.publishEvent(
            new MyEventTranslator<TestEvent>()
            {
                @Override
                public void translateTo(final TestEvent event, final long sequence)
                {
                    lastPublishedEvent = event;
                }
            });

        if (!eventCounter.await(5, TimeUnit.SECONDS))
        {
            fail("Did not process event published before start was called. Missed events: " + eventCounter.getCount());
        }
    }

    @Test
    public void shouldBatchOfEvents() throws Exception
    {
        final CountDownLatch eventCounter = new CountDownLatch(2);
        disruptor.handleEventsWith(new MyEventHandler<TestEvent>()
        {
            @Override
            public void onEvent(final TestEvent event, final long sequence, final boolean endOfBatch) throws Exception
            {
                eventCounter.countDown();
            }
        });

        disruptor.start();

        disruptor.publishEvents(
            new MyEventTranslatorOneArg<TestEvent, Object>()
            {
                @Override
                public void translateTo(final TestEvent event, final long sequence, Object arg)
                {
                    lastPublishedEvent = event;
                }
            },
            new Object[] { "a", "b" }
        );

        if (!eventCounter.await(5, TimeUnit.SECONDS))
        {
            fail("Did not process event published before start was called. Missed events: " + eventCounter.getCount());
        }
    }

    @Test
    public void shouldAddEventProcessorsAfterPublishing() throws Exception
    {
        MyRingBuffer<TestEvent> rb = disruptor.getRingBuffer();
        MyBatchEventProcessor<TestEvent> b1 = new MyBatchEventProcessor<TestEvent>(
            rb, rb.newBarrier(), new SleepingEventHandler());
        MyBatchEventProcessor<TestEvent> b2 = new MyBatchEventProcessor<TestEvent>(
            rb, rb.newBarrier(b1.getSequence()), new SleepingEventHandler());
        MyBatchEventProcessor<TestEvent> b3 = new MyBatchEventProcessor<TestEvent>(
            rb, rb.newBarrier(b2.getSequence()), new SleepingEventHandler());

        assertThat(b1.getSequence().get(), is(-1L));
        assertThat(b2.getSequence().get(), is(-1L));
        assertThat(b3.getSequence().get(), is(-1L));

        rb.publish(rb.next());
        rb.publish(rb.next());
        rb.publish(rb.next());
        rb.publish(rb.next());
        rb.publish(rb.next());
        rb.publish(rb.next());

        disruptor.handleEventsWith(b1, b2, b3);

        assertThat(b1.getSequence().get(), is(5L));
        assertThat(b2.getSequence().get(), is(5L));
        assertThat(b3.getSequence().get(), is(5L));
    }


    private void createDisruptor() {
        executor = new StubThreadFactory();
        createDisruptor(executor);
    }

    private void createDisruptor(final ThreadFactory threadFactory) {
        disruptor = new MyDisruptor<TestEvent>(
            TestEvent.EVENT_FACTORY, 4, threadFactory,
            MyProducerType.SINGLE, new MyBlockingWaitStrategy());
    }
}
