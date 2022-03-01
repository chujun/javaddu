package com.jun.chu.java.json.jackson;

import lombok.Data;

/**
 * @author chujun
 * @date 2022/3/1
 */
@Data
public class Organization {
    private People people;

    @Data
    public static class People {
        private Integer id;
    }

    @Data
    public static class Man extends People {
        private String manField;
    }

    @Data
    public static class Woman extends People {
        private String womanField;
    }
}
