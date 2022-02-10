/**
 * 1、pipeline简介
 * pipeline又称为管道，是一种在计算机普遍使用的技术。
 * 举个最普遍的例子，如下图所示cpu流水线，一个流水线分为4部分，每个部分可以独立工作，于是可以处理多个数据流。
 * linux 管道也是一个常用的管道技术，其字符处理功能十分强大，在面试过程中常会被问到。
 * 在分布式处理领域，由于管道模式是数据驱动，
 * 而目前流行的Spark分布式处理平台也是数据驱动的，
 * 两者非常合拍，于是在spark的新的api里面pipeline模式得到了广泛的应用。
 * 还有java web中的struct的filter、netty的pipeline，无处不见pipeline模式。
 * 因此，本小结的目标是使用java编写一个简易的pipeline小程序，并进行相应的功能，性能 测试。
 *
 *
 *
 * 本节的目的是实现一个并行字符串处理程序。设计思路如下图所示，参考了netty的管道模型。
 *
 * https://codeantenna.com/a/XBcBNtEYGb
 * @author chujun
 * @date 2022/2/10
 * java pipeline模式
 */
package com.jun.chu.java.pipeline;