package com.jun.chu.java.benchmark.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author chujun
 * @date 2022/2/13
 * @see 对OperationsPerInvocation注解有疑问参见 https://www.cxybb.com/article/weixin_42348333/85857480
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
@State(Scope.Benchmark)
public class CacheAccessBenchmark {
    /*
     * This sample serves as a warning against subtle differences in cache access patterns.
     *
     * Many performance differences may be explained by the way tests are accessing memory.
     * In the example below, we walk the matrix either row-first, or col-first:
     */

    private final static int COUNT = 4096;
    private final static int MATRIX_SIZE = COUNT * COUNT;

    private int[][] matrix;

    @Setup
    public void setup() {
        matrix = new int[COUNT][COUNT];
        Random random = new Random(1234);
        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < COUNT; j++) {
                matrix[i][j] = random.nextInt();
            }
        }
    }

    @Benchmark
    @OperationsPerInvocation(MATRIX_SIZE)
    public void colFirst(Blackhole bh) {
        for (int c = 0; c < COUNT; c++) {
            for (int r = 0; r < COUNT; r++) {
                bh.consume(matrix[r][c]);
            }
        }
    }

    /**
     * 速度更快，java二位数组结构是一维数组的一维数组组成的
     */
    @Benchmark
    @OperationsPerInvocation(MATRIX_SIZE)
    public void rowFirst(Blackhole bh) {
        for (int r = 0; r < COUNT; r++) {
            for (int c = 0; c < COUNT; c++) {
                bh.consume(matrix[r][c]);
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(".*" + CacheAccessBenchmark.class.getSimpleName() + ".*")
            .build();

        new Runner(opt).run();
    }

    /*
        Notably, colFirst accesses are much slower, and that's not a surprise: Java's multidimensional
        arrays are actually rigged, being one-dimensional arrays of one-dimensional arrays. Therefore,
        pulling n-th element from each of the inner array induces more cache misses, when matrix is large.
        -prof perfnorm conveniently highlights that, with >2 cache misses per one benchmark op:

        Benchmark                                                 Mode  Cnt   Score    Error  Units
        JMHSample_37_MatrixCopy.colFirst                          avgt   25   5.306 Â±  0.020  ns/op
        JMHSample_37_MatrixCopy.colFirst:Â·CPI                     avgt    5   0.621 Â±  0.011   #/op
        JMHSample_37_MatrixCopy.colFirst:Â·L1-dcache-load-misses   avgt    5   2.177 Â±  0.044   #/op <-- OOPS
        JMHSample_37_MatrixCopy.colFirst:Â·L1-dcache-loads         avgt    5  14.804 Â±  0.261   #/op
        JMHSample_37_MatrixCopy.colFirst:Â·LLC-loads               avgt    5   2.165 Â±  0.091   #/op
        JMHSample_37_MatrixCopy.colFirst:Â·cycles                  avgt    5  22.272 Â±  0.372   #/op
        JMHSample_37_MatrixCopy.colFirst:Â·instructions            avgt    5  35.888 Â±  1.215   #/op

        JMHSample_37_MatrixCopy.rowFirst                          avgt   25   2.662 Â±  0.003  ns/op
        JMHSample_37_MatrixCopy.rowFirst:Â·CPI                     avgt    5   0.312 Â±  0.003   #/op
        JMHSample_37_MatrixCopy.rowFirst:Â·L1-dcache-load-misses   avgt    5   0.066 Â±  0.001   #/op
        JMHSample_37_MatrixCopy.rowFirst:Â·L1-dcache-loads         avgt    5  14.570 Â±  0.400   #/op
        JMHSample_37_MatrixCopy.rowFirst:Â·LLC-loads               avgt    5   0.002 Â±  0.001   #/op
        JMHSample_37_MatrixCopy.rowFirst:Â·cycles                  avgt    5  11.046 Â±  0.343   #/op
        JMHSample_37_MatrixCopy.rowFirst:Â·instructions            avgt    5  35.416 Â±  1.248   #/op

        So, when comparing two different benchmarks, you have to follow up if the difference is caused
        by the memory locality issues.
     */
}
