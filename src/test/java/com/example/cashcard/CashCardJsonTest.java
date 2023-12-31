package com.example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


// JsonTest : Jackson framework
// This provides extensive JSON testing and parsing support
// It also establishes all the related behavior to test JSON objects
@JsonTest
public class CashCardJsonTest {

    // JacksonTester : convenience wrapper to Jackson JSON parsing library
    // It handles serialization and deserialization of JSON objects
    @Autowired
    private JacksonTester<CashCard>json;

    @Test
    void myFirstTest(){
        assertThat(42).isEqualTo(42);

    }


    /*
    Deserialization

    - the reverse process of serialization
    - It transforms data from a file or byte stream back into an object for application
    - This makes it possible for an object serialized on one platform to be deserialized on a different platform
    - Serialization and deserialization work together to transform/recreate data objects to/from a portable format
    The most popular data format for serializing data is JSON

     */
    @Test
    void cashCardSerializationTest() throws IOException {

        CashCard cashCard = new CashCard(99L, 123.45);
        // Unable to load JSON from class path resource
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);

    }


    @Test
    void cashCardDeserializationTest() throws IOException{
        String expected = """
                {
                    "id" : 99,
                    "amount" : 123.45
                }
                """;

        assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 123.45));

        assertThat(json.parseObject(expected).id()).isEqualTo(99L);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);

    }

}
