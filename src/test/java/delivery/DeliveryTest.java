package delivery;

import com.google.gson.Gson;
import dto.OrderRealDto;
import helpers.SetupFunctions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class DeliveryTest {

    public static String token;

    @BeforeAll
    public static void setup() {
        System.out.println("---> test start");

        SetupFunctions setupFunctions = new SetupFunctions();

        String baseUrl = setupFunctions.getBaseUrl();
        String username = setupFunctions.getUsername();
        String pwd = setupFunctions.getPassword();

        System.out.println("---> Token received: " + setupFunctions.getToken());

        token = setupFunctions.getToken();

        RestAssured.baseURI = setupFunctions.getBaseUrl();


    }

    @Test
    public void createOrderTest() {

        OrderRealDto orderRealDto = new OrderRealDto("testname", "12345", "commentarii");
//1
        Gson gson = new Gson();
//2
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(gson.toJson(orderRealDto))
                .log()
                .all()
                .post("/orders")
                .then()
                .log()
                .all()
                .extract()
                .response();

    }

    @Test
    public void createOrderWithEmptyCommentTest() {

        OrderRealDto orderRealDto = new OrderRealDto("testname", "76567890", "");

        Gson gson = new Gson();

        String receivedComment = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(gson.toJson(orderRealDto))
                .log()
                .all()
                .post("/orders")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("comment");

        Assertions.assertEquals("", receivedComment);
    }

    @Test
    public void createOrderWithoutTokenTest() {

        OrderRealDto orderRealDto = new OrderRealDto("testusername", "76567890", "");

        Gson gson = new Gson();

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer ")
                .body(gson.toJson(orderRealDto))
                .log()
                .all()
                .post("/orders")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void getOrderById() {

        int id = orderCreationPrecondition();
        int receivedId = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log()
                .all()
                .get("/orders" + "/" + id)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("id");
        Assertions.assertEquals(receivedId, id);

    }

    @Test
    public void getNonExistingOrderById() {

        int id = 3000;
        String response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log()
                .all()
                .get("/orders" + "/" + id)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .asString();

        Assertions.assertEquals("", response);

    }

    //Delete all orders, then create some orders and check numbers of created orders
    @Test
    public void getOrders() {

        int id = orderCreationPrecondition();
        OrderRealDto[] orderRealDtoArray = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log()
                .all()
                .get("/orders")
                .then()
                .log()
                .all()
                .extract()
                .as(OrderRealDto[].class);

        // Delete all orders by ids
        for (int i = 0; i < orderRealDtoArray.length; i++) {
            deleteOrderById(orderRealDtoArray[i].getId());
        }

        // System.out.println();


        // List<OrderRealDto> list = given().header()

    }

    @Test
    public void deleteOrderByIdTest() {
        deleteOrderById(2806);
    }

    //Method creates order and returns its id
    public int orderCreationPrecondition() {

        OrderRealDto orderRealDto = new OrderRealDto("testname", "1234567", "no");
        Gson gson = new Gson();

        int id = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(gson.toJson(orderRealDto))
                .log()
                .all()
                .post("/orders")
                .then()
                .log()
                .all()
                .extract()
                .path("id");

        return id;

    }

    //Method deletes order by id
    public void deleteOrderById(long id) {

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log()
                .all()
                .delete("/orders" + "/" + id)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

    }
}