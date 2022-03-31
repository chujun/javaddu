package com.jun.chu.java.lambda;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CollectionsToMapTest {
    @Test
    public void testToMapForDuplicateKey() {
        //不允许map key重复
        List<PropertyOptionValue> propertyOptionValues = initData();
        try {
            Map<Integer, PropertyOptionValue> collect = propertyOptionValues.stream().filter(item -> null != item)
                .collect(Collectors.toMap(PropertyOptionValue::getId, item -> item));
        } catch (IllegalStateException e) {
            //throwingMerger()
            Assert.assertEquals(true, e.getMessage().contains("Duplicate key"));
        }
    }

    @Test
    public void testToMap() {
        //允许key重复
        List<PropertyOptionValue> propertyOptionValues = initData();
        Map<Integer, PropertyOptionValue> map = propertyOptionValues.stream().filter(item -> null != item)
            .collect(Collectors.toMap(PropertyOptionValue::getId, item -> item, (a, b) -> b));
        PropertyOptionValue propertyOptionValue = map.get(1);
        Assert.assertEquals(1, propertyOptionValue.getId().intValue());
        Assert.assertEquals("8G", propertyOptionValue.getName());
        Assert.assertEquals(2, propertyOptionValue.getVersion().intValue());

        map = propertyOptionValues.stream().filter(Objects::nonNull)
            .collect(Collectors.toMap(PropertyOptionValue::getId, item -> item, (a, b) -> a));
        propertyOptionValue = map.get(1);
        Assert.assertEquals(1, propertyOptionValue.getId().intValue());
        Assert.assertEquals("8G", propertyOptionValue.getName());
        Assert.assertEquals(1, propertyOptionValue.getVersion().intValue());
    }

    @Test
    public void testToMapSum() {
        Map<Integer, Integer> idSumMap = initData().stream()
            .collect(Collectors.toMap(PropertyOptionValue::getId,
                PropertyOptionValue::getVersion, Integer::sum));
        Assert.assertEquals(idSumMap.toString(), "{1=3, 2=6, 3=11}");
    }

    public List<PropertyOptionValue> initData() {
        List<PropertyOptionValue> result = Lists.newArrayList();
        result.add(new PropertyOptionValue(1, "8G", new Date(), 1));
        result.add(new PropertyOptionValue(2, "16G", new Date(), 1));
        result.add(new PropertyOptionValue(3, "32G", new Date(), 1));
        result.add(new PropertyOptionValue(1, "8G", new Date(), 2));
        result.add(new PropertyOptionValue(2, "32G", new Date(), 5));
        result.add(new PropertyOptionValue(3, "64G", new Date(), 10));
        return result;
    }

    @Data
    @AllArgsConstructor
    static class PropertyOptionValue {
        private Integer id;

        private String name;

        private Date createAt;

        private Integer version;
    }
}
