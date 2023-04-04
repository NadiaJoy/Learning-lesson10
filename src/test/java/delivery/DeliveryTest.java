package delivery;

import com.google.gson.Gson;
import dto.CourierCreation;
import dto.OrderRealDto;
import helpers.SetupFunctions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        //create 1 order
        int id = orderCreationPrecondition();
        //get all existing orders
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
        //check all orders after deleting
        orderRealDtoArray = given()
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

        //System.out.println(orderRealDtoArray.length);
        //System.out.println(orderRealDtoArrayAfter.length);
        Assertions.assertEquals(0, orderRealDtoArray.length);

    }

    @Test
    public void deleteOrderByIdTest() {

        int orderId = orderCreationPrecondition();
        deleteOrderById(orderId);
    }

    @Test
    public void courierOrderAvailableForbiddenForStudent() {
        Response response = executeGetMethodByStudent("/orders/available");
        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void courierOrderAssignForbiddenForStudent() {

        int orderId = orderCreationPrecondition();

        Response response = executePutMethodByStudent(String.format("/orders/%s/assign", orderId));
        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void courierOrderStatusForbiddenForStudent() {

        int orderId = orderCreationPrecondition();
        //TODO change this as it looks ugly
        String body = "{\"status\": \"OPEN\"\n}";


        Response response = executePutMethodWithBodyByStudent((String.format("/orders/%s/status", orderId)), body);
        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode());
    }

    @Test
    public void checkCreateCourierSuccessful() {

        Response response = createCourier();

        assertAll(
                "Grouped Assertions of Courier",
                () -> assertEquals(HttpStatus.SC_OK, response.getStatusCode(), "Assert Status")
                //TODO 2nd and more
                //() -> assertEquals("testname", response.asString(), "2nd Assert")
        );
    }

    //Method creates new courier
    public Response createCourier () {
        CourierCreation courierBody = new CourierCreation(generateRandomLogin(), generateRandomName(), generateRandomPhone());
        Gson gson = new Gson();

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(gson.toJson(courierBody))
                .log()
                .all()
                .post("/users/courier")
                .then()
                .log()
                .all()
                .extract()
                .response();

        return response;
    }
    //Randoms
    public String generateRandomLogin() {
        return RandomStringUtils.random(20, true, false);
    }
    public String generateRandomPhone() {
        return RandomStringUtils.random(10, false, true);
    }

    public String generateRandomName() {
        return RandomStringUtils.random(40, true, true);
    }


    //Method creates new order and returns its id
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

    //Method get for different paths
    public Response executeGetMethodByStudent(String path) {

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log()
                .all()
                .get(path)
                .then()
                .log()
                .all()
                .extract()
                .response();

        return response;
    }

    //Method Put for different paths
    public Response executePutMethodByStudent(String path) {

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log()
                .all()
                .put(path)
                .then()
                .log()
                .all()
                .extract()
                .response();

        return response;
    }


    public Response executePutMethodWithBodyByStudent(String path, String body) {

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(body)
                .log()
                .all()
                .put(path)
                .then()
                .log()
                .all()
                .extract()
                .response();

        return response;
    }
}