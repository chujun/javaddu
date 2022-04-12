package com.jun.chu.java.json.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * jackson处理多态字段梳理
 *
 * @author chujun
 * @date 2022/3/1
 */
public class JacksonPolymorphic {
    @Test
    public void test() throws IOException {
        Organization.Man man = new Organization.Man();
        man.setManField("man");
        man.setId(1);
        Organization organization = new Organization();
        organization.setPeople(man);

        ObjectMapper objectMapper = getObjectMapper();

        String json = objectMapper.writeValueAsString(organization);
        Assert.assertEquals("{\"people\":{\"id\":1,\"manField\":\"man\"}}", json);
        Organization desOrganization = objectMapper.readValue(json, Organization.class);
        Assert.assertEquals(Organization.People.class, desOrganization.getPeople().getClass());
        String desJson = objectMapper.writeValueAsString(desOrganization);
        //子类信息丢失
        Assert.assertEquals("{\"people\":{\"id\":1}}", desJson);

    }

    @Test
    public void testOrganizationV2() throws IOException {
        OrganizationV2.ManV2 man = new OrganizationV2.ManV2();
        man.setManField("man");
        man.setId(1);
        OrganizationV2 organization = new OrganizationV2();
        organization.setPeople(man);

        ObjectMapper objectMapper = getObjectMapper();

        String json = objectMapper.writeValueAsString(organization);
        Assert.assertEquals("{\"people\":{\"@type\":\"man\",\"id\":1,\"manField\":\"man\"}}", json);
        OrganizationV2 desOrganization = objectMapper.readValue(json, OrganizationV2.class);
        Assert.assertEquals(OrganizationV2.ManV2.class, desOrganization.getPeople().getClass());
        String desJson = objectMapper.writeValueAsString(desOrganization);
        //子类信息不丢失
        Assert.assertEquals("{\"people\":{\"@type\":\"man\",\"id\":1,\"manField\":\"man\"}}", desJson);
    }


    @Test
    public void testDeserializationFeature() throws IOException {
        String json = "{\"people\":{\"@type\":\"manNew\",\"id\":1,\"manNewField\":\"man\"}}";
        ObjectMapper objectMapper = getObjectMapper();
        OrganizationV3 desOrganization = objectMapper.readValue(json, OrganizationV3.class);
        Assert.assertEquals(OrganizationV3.PeopleV3.class, desOrganization.getPeople().getClass());
        String desJson = objectMapper.writeValueAsString(desOrganization);
        //识别不了的类型，取默认类型
        Assert.assertEquals("{\"people\":{\"@type\":\"OrganizationV3$PeopleV3\",\"id\":1}}", desJson);

    }

    @Test
    public void testDeserializationFeatureV4() throws IOException {
        String json = "{\"people\":{\"@type\":\"manNew\",\"id\":1,\"manNewField\":\"man\"}}";
        ObjectMapper objectMapper = getObjectMapper();
        OrganizationV4 desOrganization = objectMapper.readValue(json, OrganizationV4.class);
        Assert.assertEquals(OrganizationV4.PeopleV4.class, desOrganization.getPeople().getClass());
        String desJson = objectMapper.writeValueAsString(desOrganization);
        //识别不了的类型，取默认类型
        Assert.assertEquals("{\"people\":{\"type\":\"OrganizationV4$PeopleV4\",\"id\":1}}", desJson);

    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}


