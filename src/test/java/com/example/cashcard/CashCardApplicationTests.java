package com.example.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

// start Spring Boot application and make it available for test to perform requests to it
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashCardApplicationTests {


    // to inject a test helper that'll allow us to make HTTP requests to locally running application
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnACashCardWhenDataIsSaved(){

        // restTemplate to make an HTTP GET request to endpoint /cashcards/99
        ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/99", String.class);

        // inspect HTTP Response Status code
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // converts the response String into a JSON-aware object with lots of helper methods
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number id = documentContext.read("$.id");
        // assert that the id is not null
//        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(99);

        Double amount = documentContext.read("$.amount");
        assertThat(amount).isEqualTo(123.45);

    }

    @Test
    void shouldNotReturnACashCardWithAnUnknownId(){

        ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/1000", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();

    }

    @Test
    void shouldCreatedANewCashCard(){

        // supplying an id to cashCardRepository.save is supported when an update is performed on an existing resource
        CashCard newCashCard = new CashCard(44L, 250.00);
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/cashcards", newCashCard, Void.class);

        // the origin server SHOULD send a 201 (Created) response containing a Location header field
        // that provides an identifier for the primary resource created
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewCashCard = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewCashCard, String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        Double amount = documentContext.read("$.amount");

        assertThat(id).isNotNull();
        assertThat(amount).isEqualTo(250.00);



    }





}
