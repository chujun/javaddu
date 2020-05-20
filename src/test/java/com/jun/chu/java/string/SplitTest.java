package com.jun.chu.java.string;

import com.google.common.collect.Sets;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chujun
 * @date 2020-05-20
 */
public class SplitTest {
    @Test
    public void commonSplit() {
        Assert.assertEquals(Collections.emptySet(), split(null));
        Assert.assertEquals(Collections.emptySet(), split(""));
        Assert.assertEquals(Sets.newHashSet(1), split("1"));
        Assert.assertEquals(Sets.newHashSet(1, 2), split("1,2"));
        Assert.assertEquals(Sets.newHashSet(1, 2, 3), split("1,2,3"));
    }

    private Set<Integer> split(String str) {
        String[] split = StringUtils.split(str, ",");
        if (ArrayUtils.isEmpty(split)) {
            return Collections.emptySet();
        }
        return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toSet());
    }
}
