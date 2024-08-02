package com.cj.netty.d1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Files文件目录遍历树
 * 需求:例如统计一个目录下所有目录数和文件数
 *
 * @author chujun
 * @date 2024/7/30
 */
@Slf4j
public class TestFilesWalkTree {
    private static final AtomicInteger directoryCount = new AtomicInteger(0);
    private static final AtomicInteger fileCount = new AtomicInteger(0);
    private static final AtomicInteger jarCount = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        //访问者设计模式
        Files.walkFileTree(Paths.get("/Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home"),
            new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                    directoryCount.incrementAndGet();
                    log.debug("path:{}", dir);
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    fileCount.incrementAndGet();
                    //log.debug("fileName:{}", file.getFileName());
                    if (file.toString().endsWith(".jar")) {
                        log.debug("jar file:{}", file);
                        jarCount.incrementAndGet();
                    }
                    return super.visitFile(file, attrs);
                }
            });
        log.debug("directoryCount:{},fileCount:{},jarCount:{}",
            directoryCount, fileCount, jarCount);
    }
}
