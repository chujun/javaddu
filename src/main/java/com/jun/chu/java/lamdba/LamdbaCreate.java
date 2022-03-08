package com.jun.chu.java.lamdba;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author chujun
 * @date 2022/3/8
 */
public class LamdbaCreate {
    public static <T> void isTrue(boolean result, T t, Consumer<T> consumer) {
        if (result) {
            consumer.accept(t);
        }
    }

    public static <T, R> R isTrueForFunction(boolean result, T t, R r, Function<T, R> function) {
        if (result) {
            return function.apply(t);
        }
        return r;
    }

    public static void main(String[] args) {
        Integer a = null;
        Number number = new Number();
        number.setCount(0);


        LamdbaCreate.isTrueForFunction(Objects.nonNull(a), a, number, e -> number.append(e));

        Integer count = null;
        Integer number2 = 1;
        String address = "";
        String name = "name";
        TestA test = new TestA();

        ChainObject.get()
            .eq(Objects.nonNull(count), () -> test.setCount(count))
            .eq(Objects.nonNull(number), () -> test.setNumber(number2))
            .eq(StringUtils.isNotBlank(address), () -> test.setAddress(address))
            .eq(StringUtils.isNotBlank(name), () -> test.setName(name));


        System.out.println(test);
    }

    @Data
    public static class Number {
        private int count;

        public Number append(Integer a) {
            count += a;
            return this;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestA {
        private Integer count;

        private String name;

        private String address;

        private Integer number;
    }

}
