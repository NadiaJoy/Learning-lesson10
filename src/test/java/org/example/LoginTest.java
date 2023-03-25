package org.example;

import dto.CredentialsDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LoginTest {


    @BeforeEach
    public void setup() {
        System.out.println("---> Test start");
        RestAssured.baseURI = "http://51.250.6.164";
        RestAssured.port = 8080;
    }


    @Test
    public void positiveLoginTest () {

        CredentialsDto credentials = new CredentialsDto();

        credentials.setUsername("dianadia");
        credentials.setPassword("hellouser123");

        Response response = given()
                .header("Content-type", "application/json")
                .body(credentials)
                .log()
                .all()
                .post("/login/student")
                .then()
                .log()
                .all()
                .extract()
                .response();

        Assertions.assertNotNull(response.asString());
    }

    @Test
    public void wrongPasswordTest () {

        CredentialsDto credentials = new CredentialsDto();

        credentials.setUsername("dianadia");
        credentials.setPassword("wrongPass123456");

          given()
                .header("Content-type", "application/json")
                .body(credentials)
                .log()
                .all()
                .post("/login/student")
                .then()
                .log()
                .all()
                  .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    public void voidLoginAndPasswordTest () {

        CredentialsDto credentials = new CredentialsDto();

        credentials.setUsername("");
        credentials.setPassword("");

        given()
                .header("Content-type", "application/json")
                .body(credentials)
                .log()
                .all()
                .post("/login/student")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

}
