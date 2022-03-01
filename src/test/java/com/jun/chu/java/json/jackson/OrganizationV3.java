package com.jun.chu.java.json.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author chujun
 * @date 2022/3/1
 */
@Data
public class OrganizationV3 {
    private PeopleV3 people;

    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY
        , defaultImpl = PeopleV3.class
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = ManV3.class, name = "man"),
        @JsonSubTypes.Type(value = WomanV3.class, name = "woman"),
    })
    public static class PeopleV3 {
        private Integer id;
    }

    @Data
    public static class ManV3 extends PeopleV3 {
        private String manField;
    }

    @Data
    public static class WomanV3 extends PeopleV3 {
        private String womanField;
    }
}
