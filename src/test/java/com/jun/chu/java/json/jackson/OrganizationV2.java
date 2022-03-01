package com.jun.chu.java.json.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author chujun
 * @date 2022/3/1
 */
@Data
public class OrganizationV2 {
    private PeopleV2 people;

    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY
        //,defaultImpl = PeopleV2.class
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = ManV2.class, name = "man"),
        @JsonSubTypes.Type(value = WomanV2.class, name = "woman"),
    })
    public static class PeopleV2 {
        private Integer id;
    }

    @Data
    public static class ManV2 extends PeopleV2 {
        private String manField;
    }

    @Data
    public static class WomanV2 extends PeopleV2 {
        private String womanField;
    }
}
