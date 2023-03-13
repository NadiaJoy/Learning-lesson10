package org.example;

import dto.OrderDto;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;

public class ApiTest {

    @BeforeEach
    public void setup() {
        System.out.println("---> Test start");
        RestAssured.baseURI = "http://51.250.6.164";
        RestAssured.port = 8080;
    }

    @Test
    public void simplePositiveTest() {

        given().
                log().
                all().
                when().
                get("/test-orders/5").
//                get("http://51.250.6.164:8080/test-orders/5").
        then().
                log().
                all().
                statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 9, 10})
    public void getPositiveTest200(int id) {
        given().
                log().
                all().
                when().
                get("/test-orders/{id}", id).
                then().
                log().
                all().
                statusCode(HttpStatus.SC_OK);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 11, -1})
    public void getNegativeTest400(int id) {

        given().
                log().
                all().
                when().
                get("/test-orders/{id}", id).
                then().
                log().
                all().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    String order = "{\"customerName\":\"name\",\"customerPhone\":\"123456\",\"comment\":\"comment\"}";

    @Test
    public void createOrderAndCheckStatusCode200() {
       // OrderDto orderdto = new OrderDto("testname", "123456", "comment");

        OrderDto orderDtoRandom = new OrderDto();
        orderDtoRandom.setCustomerName( generateRandomName() );
        orderDtoRandom.setCustomerPhone(RandomStringUtils.random(10, false, true));
        orderDtoRandom.setComment(RandomStringUtils.random(40, true, true));

        given()
                .header("Content-type", "application/json")
                .body(orderDtoRandom)
                .log()
                .all()
                .post("/test-orders")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void negativeCreateWrongOrderAndCheckStatusCode415() {

        given()
                .body(order)
                .log()
                .all()
                .post("/test-orders")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 9, 10})
    public void checkBodyResponseStatusOpen(int id) {
        String status =
                given().
                        log().
                        all().
                        when().
                        get("/test-orders/{id}", id).
                        then().
                        log().
                        all().
                        statusCode(HttpStatus.SC_OK).
                        and().
                        extract().
                        path("status");

        Assertions.assertEquals(status, "OPEN");
    }

    public String generateRandomName () {
        return RandomStringUtils.random (20, true, false);


    }
}
