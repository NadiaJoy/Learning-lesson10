package delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.gson.Gson;
import db.DBmanager;
import dto.OrderRealDto;
import helpers.SetupFunctions;
import helpers.Status;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IntegrationTest {

    static Connection connection;
    static DBmanager dBmanager;
    static String token;

    static String baseUrlUI;
    static String username;
    static String password;

    @BeforeAll
    public static void setUp() {
        //open connection
        dBmanager = new DBmanager();
        connection = dBmanager.connect();

        Assumptions.assumeTrue(connection != null, "No connection to DB. Infrastructure failure");

        SetupFunctions setupFunctions = new SetupFunctions();
        System.out.println("token: " + setupFunctions.getToken());
        token = setupFunctions.getToken();
        RestAssured.baseURI = setupFunctions.getBaseUrl();

        baseUrlUI = setupFunctions.getBaseUrlUI();
        username = setupFunctions.getUsername();
        password = setupFunctions.getPassword();
    }

    @AfterAll
    public static void tearDown() {
        //close connection
        dBmanager.close(connection);

    }


    @Test
    public void createOrderUsingWebAndCheckDb() {
        open(baseUrlUI);
        SelenideElement nameInput = $(By.id("username")).setValue(username);
        SelenideElement passInput = $(By.id("password")).setValue(password);
        $(By.xpath("//*[@data-name='signIn-button']")).click();

        $(By.id("name")).setValue(generateRandomNameForOrder());
        $(By.id("phone")).setValue(generateRandomPhoneNumberForOrder());
        $(By.xpath("//*[@data-name='createOrder-button']")).click();


        String successText = $(By.xpath("//*[@data-name='orderSuccessfullyCreated-popup-close-button']/following-sibling::span"))
                .shouldBe(Condition.visible)
                .getAttribute("innerHTML");

        int orderId = Integer.parseInt(successText.replaceAll("[^0-9]", ""));

        executeSearchAndCompare(orderId);

    }

    // HW 21
    @Test
    public void createOrderUsingApiAndCheckBySearchInWebApp() {
        //step1. create an order using API
        String name = generateRandomNameForOrder();
        String phone = generateRandomPhoneNumberForOrder();
        String comment = generateRandomCommentForOrder();
        int orderId = orderCreationPrecondition(name, phone, comment);
        //step2. check in web app
        //2.1. login using login page class
        open(baseUrlUI);
        LoginPage loginPage = new LoginPage();
        loginPage.insertLogin(username);
        loginPage.insertPassword(password);
        loginPage.clickLogin();
        //2.2. open status page
        $(By.xpath("//*[@data-name='openStatusPopup-button']")).click();
        //2.3. search the created order
        $(By.xpath("//*[@data-name='searchOrder-input']")).setValue(String.valueOf(orderId));
        $(By.xpath("//*[@data-name='searchOrder-submitButton']")).click();
        //step 3. check the fields are OK
        try {
            String nameText = $(By.xpath("//*[@data-name='order-item-0']/span")).shouldBe(Condition.visible).getAttribute("innerHTML");
            String phoneText = $(By.xpath("//*[@data-name='order-item-1']/span")).shouldBe(Condition.visible).getAttribute("innerHTML");
            String commentText = $(By.xpath("//*[@data-name='order-item-2']/span")).shouldBe(Condition.visible).getAttribute("innerHTML");

            assertAll(
                    "Grouped Assertions for Order",
                    () -> Assertions.assertEquals(name, nameText, "Name is not OK!"),
                    () -> Assertions.assertEquals(phone, phoneText, "Phone is not OK!"),
                    () -> Assertions.assertEquals(comment, commentText, "Comment is not OK!")
            );
            //it seems not working as I want
        } catch (NoSuchElementException e) {
            System.out.println("Order page not found");
            //e.printStackTrace();
            Assertions.fail("No Such Element Exception");

        }

    }

    //method searches orderId in DB and checks status is OPEN
    public void executeSearchAndCompare(int orderId) {

        //step 1
        String sql = String.format("Select * from orders  id = %d;", orderId);

        System.out.println();

        try {
            System.out.println("Executing SQL...");
            System.out.println("SQL is :" + sql);
            //Step 2
            Statement statement = connection.createStatement();
            //Step 3
            ResultSet resultSet = statement.executeQuery(sql);

            int size = 0;
            String statusFromDb = null;

            if (resultSet != null) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
                    statusFromDb = resultSet.getString(3);
                    size++;
                }

                Assertions.assertEquals(1, size);

                Assertions.assertEquals(Status.OPEN.toString(), statusFromDb);
            } else {
                Assertions.fail("Result set is null");

            }

        } catch (SQLException e) {
            System.out.println("Error while executing sql ");
            System.out.println(e.getErrorCode());
            System.out.println(e.getSQLState());
            e.printStackTrace();

            Assertions.fail("SQLException");

        }
    }

    //Method creates new order and returns its id
    public int orderCreationPrecondition(String name, String phone, String comment) {

        OrderRealDto orderRealDto = new OrderRealDto(name, phone, comment);
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

    //Randoms
    public String generateRandomNameForOrder() {
        return RandomStringUtils.random(8, true, false);
    }

    public String generateRandomPhoneNumberForOrder() {
        return RandomStringUtils.random(8, false, true);
    }

    public String generateRandomCommentForOrder() {
        return RandomStringUtils.random(8, true, true);
    }


}
