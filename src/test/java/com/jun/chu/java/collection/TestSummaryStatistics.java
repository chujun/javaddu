package com.jun.chu.java.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.IntSummaryStatistics;
import java.util.Set;

/**
 * @author jun.chu
 * @date 2019-02-20 18:21
 */
public class TestSummaryStatistics {
    @Test
    public void testIntSummaryStatistics() {
        Assert.assertEquals(9, Lists.newArrayList(1, 3, 5)
            .stream().mapToInt(item -> item).sum());
        Set<Integer> timeScopePoints = Sets.newHashSet();
        IntSummaryStatistics intSummaryStatistics = timeScopePoints.stream().mapToInt(item -> item).summaryStatistics();
        Assert.assertEquals(Integer.MIN_VALUE, intSummaryStatistics.getMax());
        Assert.assertEquals(Integer.MAX_VALUE, intSummaryStatistics.getMin());
        timeScopePoints.add(1);
        intSummaryStatistics = timeScopePoints.stream().mapToInt(item -> item).summaryStatistics();
        Assert.assertEquals(1, intSummaryStatistics.getMax());
        Assert.assertEquals(1, intSummaryStatistics.getMin());
        timeScopePoints.add(2);
        intSummaryStatistics = timeScopePoints.stream().mapToInt(item -> item).summaryStatistics();
        Assert.assertEquals(2, intSummaryStatistics.getMax());
        Assert.assertEquals(1, intSummaryStatistics.getMin());
    }
}
