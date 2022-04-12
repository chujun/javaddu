package com.jun.chu.java.json.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * 这种写法比较复杂,还是推荐v3版本写法
 * @author chujun
 * @date 2022/3/1
 */
@Data
public class OrganizationV4 {
    private PeopleV4 people;

    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = PeopleV4.class
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = ManV4.class, name = "man"),
        @JsonSubTypes.Type(value = WomanV4.class, name = "woman"),
    })
    public static class PeopleV4 {
        private Integer id;
    }

    @Data
    public static class ManV4 extends PeopleV4 {
        private String manField;
    }

    @Data
    public static class WomanV4 extends PeopleV4 {
        private String womanField;
    }
}
