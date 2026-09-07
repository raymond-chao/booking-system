package com.example.customer_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class CustomerServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllCustomersReturnsOk() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/customers", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void createCustomerReturnsCreated() {

        String customerJson = """
            {
              "name": "Test User",
              "email": "test@example.com",
              "phoneNumber": "0701234567",
              "password": "password123"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(customerJson, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "/api/customers",
                        request,
                        String.class
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    void getCustomerThatDoesNotExistReturnsNotFound() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        "/api/customers/999999",
                        String.class
                );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}