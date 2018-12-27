package com.jun.chu.java.string;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author jun.chu
 * @date 2018-12-27 17:55
 */
public class JoinMethodTest {
    @Test
    public void testJoin() {
        String skuIdsStr = join(Lists.newArrayList(1, 2, 3));
        Assert.assertEquals("1,2,3", skuIdsStr);
        Assert.assertEquals("", join(Lists.newArrayList()));
    }

    public String join(List<Integer> list) {
        return StringUtils.join(list, ",");
    }

}
