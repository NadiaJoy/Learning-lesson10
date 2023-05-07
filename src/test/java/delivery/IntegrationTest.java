package delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import db.DBmanager;
import helpers.SetupFunctions;
import helpers.Status;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

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
    public void createOrderUsingWebAndCheckDb () {
        open(baseUrlUI);
        SelenideElement nameInput = $(By.id ("username")).setValue(username);
        SelenideElement passInput = $(By.id ("password")).setValue(password);
        $(By.xpath("//*[@data-name='signIn-button']")).click();

        $(By.id ("name")).setValue(generateRandomNameForOrder());
        $(By.id ("phone")).setValue(generateRandomPhoneNumberForOrder());
        $(By.xpath("//*[@data-name='createOrder-button']")).click();


        String successText = $(By.xpath("//*[@data-name='orderSuccessfullyCreated-popup-close-button']/following-sibling::span"))
                .shouldBe(Condition.visible)
                .getAttribute("innerHTML");

        int orderId = Integer.parseInt (successText.replaceAll("[^0-9]",""));

        executeSearchAndCompare(orderId);

    }


    public void executeSearchAndCompare(int orderId) {

        //step 1
        String sql = String.format("Select * from orders where id = %d;", orderId);

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

    //Randoms
    public String generateRandomNameForOrder() {
        return RandomStringUtils.random(8, true, false);
    }
    public String generateRandomPhoneNumberForOrder() {
        return RandomStringUtils.random(8, false, true);
    }


}
