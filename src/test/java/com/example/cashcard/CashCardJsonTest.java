package com.example.cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
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
    
    
    @Autowired
    private JacksonTester<CashCard[]> jsonList;
    
    private CashCard[] cashCards;


    @BeforeEach
    void setUp() {
        cashCards = Arrays.array(
                new CashCard(99L, 123.45, "sarah1"),
                new CashCard(100L, 1.00, "sarah1"),
                new CashCard(101L, 150.00, "sarah1"));
    }

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

        CashCard cashCard = cashCards[0];
        // Unable to load JSON from class path resource
        /*
            Select the "Mark Directory as..." sub-menu > Select "Test Resources Root"
         */
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
                    "id": 99,
                    "amount": 123.45, 
                    "owner": "sarah1"
                }
                """;

        assertThat(json.parse(expected))
                .isEqualTo(new CashCard(99L, 123.45, "sarah1"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99L);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);

    }


    @Test
    void cashCardListSerializationTest() throws IOException{


        // It serializes the cashCards variable into JSON, then asserts that list.json should contain
        // the same data as the serialized cashCards variable
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }


    @Test
    void cashCardListDeserializationTest() throws IOException{

        String expected = """
                [
                     {"id": 99, "amount": 123.45 , "owner": "sarah1"},
                     {"id": 100, "amount": 1.00 , "owner": "sarah1"},
                     {"id": 101, "amount": 150.00, "owner": "sarah1"}
                                                  
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }




    

}
