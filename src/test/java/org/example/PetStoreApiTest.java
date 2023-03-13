package org.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;

public class PetStoreApiTest {

    @BeforeEach
    public void setup() {
        System.out.println("---> Test start");
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @ParameterizedTest
    @ValueSource(ints = {2,7})
    public void getPositiveTest200(int orderId) {
        given().
                log().
                all().
                when().
                get("/store/order/{orderId}", orderId).
                then().
                log().
                all().
                statusCode(HttpStatus.SC_OK);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 5, 6, 8, 9, 10})
    public void getPositiveTest404(int orderId) {
        given().
                log().
                all().
                when().
                get("/store/order/{orderId}", orderId).
                then().
                log().
                all().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void getNegativeTest404(int orderId) {
        given().
                log().
                all().
                when().
                get("/store/order/{orderId}", orderId).
                then().
                log().
                all().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,7})
    public void checkBodyResponseStatusApproved(int orderId) {
        String status =
                given().
                        log().
                        all().
                        when().
                        get("/store/order/{orderId}", orderId).
                        then().
                        log().
                        all().
                        statusCode(HttpStatus.SC_OK).and().extract().path("status");

        Assertions.assertEquals(status, "approved");
    }

}
