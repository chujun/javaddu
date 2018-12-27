package com.jun.chu.java.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * @author jun.chu
 * @date 2018-12-26 18:42
 */
public class TestObjects {
    @Test
    public void testEquals(){
        Assert.assertEquals(false, Objects.equals(true,null));
    }


}
