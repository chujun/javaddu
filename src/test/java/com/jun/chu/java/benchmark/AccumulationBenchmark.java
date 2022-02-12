package com.jun.chu.java.benchmark;

import org.junit.Assert;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

/**
 * 使用@Param参数调整测试方法入参值
 *
 * @author chujun
 * @date 2022/2/12
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class AccumulationBenchmark {
    @Param({"10000", "100000", "1000000", "10000000"})
    private int length;

    private long[] numbers;
    private long sumResult;

    @Benchmark
    public void streamBench() {
        long sum = Arrays.stream(numbers).sum();
        Assert.assertEquals(sumResult, sum);
    }

    @Benchmark
    public void parallelStreamBench() {
        long sum = Arrays.stream(numbers).parallel().sum();
        Assert.assertEquals(sumResult, sum);
    }

    @Benchmark
    public void foreachBench() {
        long sum = 0;
        for (final long number : numbers) {
            sum += number;
        }
        Assert.assertEquals(sumResult, sum);
    }

    @Benchmark
    public void extraCost() {
        Assert.assertEquals(sumResult, sumResult);
    }

    private static long sum(long[] numbers) {
        long sum = 0;
        for (final long number : numbers) {
            sum += number;
        }
        return sum;
    }

    @Setup
    public void prepare() {
        numbers = LongStream.rangeClosed(1, length).toArray();
        sumResult = sum(numbers);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
            .include(AccumulationBenchmark.class.getSimpleName())
            .forks(1)
            .warmupIterations(3)
            .measurementIterations(3)
            .output("/tmp/benchmark/AccumulationBenchmark.out")
            .build();
        new Runner(opt).run();
    }
}
