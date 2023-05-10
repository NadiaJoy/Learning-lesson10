package delivery;

import helpers.SetupFunctions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;



public class WebAppTest {
    static String baseUrlUI;
    static String username;
    static String pwd;

    @BeforeAll
    public static void setUp() {

        SetupFunctions setupFunctions = new SetupFunctions();

        baseUrlUI = setupFunctions.getBaseUrlUI();
        username = setupFunctions.getUsername();
        pwd = setupFunctions.getPassword();
    }

    @BeforeEach
    public void setup() {
        open(baseUrlUI);
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }


    @Test
    public void incorrectLogin() {

        LoginPage loginPage = new LoginPage();
        loginPage.insertLogin("hello");
        loginPage.insertPassword("wrongpasswwoorrd");
        loginPage.clickLogin();
        loginPage.checkErrorPopup();

    }

    @Test
    public void successfulLogin() {

        LoginPage loginPage = new LoginPage();
        loginPage.insertLogin(username);
        loginPage.insertPassword(pwd);
        loginPage.clickLogin();
        loginPage.checkSuccessfulLogin();

    }
}
