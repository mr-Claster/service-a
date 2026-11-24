package com.example;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.ProcessingException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

@QuarkusTest
class GreetingResourceTest {

    @InjectMock
    @RestClient
    NameClient nameClient;

    @Test
    void returnsGreetingWhenServiceBResponds() {
        when(nameClient.getName(anyString()))
                .thenReturn(new NameClient.NameResponse("Марія"));

        given()
                .when().get("/greeting")
                .then()
                .statusCode(200)
                .body("name", equalTo("Марія"))
                .body("message", equalTo("Привіт! Твоє ім’я — Марія."));
    }

    @Test
    void returns503WhenServiceBIsUnavailable() {
        when(nameClient.getName(anyString()))
                .thenThrow(new ProcessingException("Connection refused"));

        given()
                .when().get("/greeting")
                .then()
                .statusCode(503)
                .body("error", equalTo("Не вдалося отримати відповідь від service-b."));
    }
}