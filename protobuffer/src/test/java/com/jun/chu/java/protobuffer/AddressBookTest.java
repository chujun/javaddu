package com.jun.chu.java.protobuffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 00065906
 * @date 2022/8/5
 */
public class AddressBookTest {
    @Test
    public void test(){
        Person john =
            Person.newBuilder()
                .setId(1234)
                .setName("John Doe")
                .setEmail("jdoe@example.com")
                .addPhones(
                    Person.PhoneNumber.newBuilder()
                        .setNumber("555-4321")
                        .setType(Person.PhoneType.HOME))
                .build();
        Assert.assertEquals("name: \"John Doe\"\n" +
            "id: 1234\n" +
            "email: \"jdoe@example.com\"\n" +
            "phones {\n" +
            "  number: \"555-4321\"\n" +
            "  type: HOME\n" +
            "}\n",john.toString());
    }
}
