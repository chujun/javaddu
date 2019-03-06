package com.jun.chu.java.string;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jun.chu
 * @date 2019-03-06 18:21
 */
public class ReplaceTest {
    @Test
    public void testReplace() {
        String str = "123322123";
        String result = StringUtils.replace(str, "1", "hao");
        Assert.assertEquals("hao23322hao23", result);
    }
}
