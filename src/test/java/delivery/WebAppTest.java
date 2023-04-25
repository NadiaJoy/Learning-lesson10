package delivery;

import helpers.SetupFunctions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;


public class WebAppTest {
    SetupFunctions setupFunctions = new SetupFunctions();

    public String baseUrlUI = setupFunctions.getBaseUrlUI();
    public String username = setupFunctions.getUsername();
    public String pwd = setupFunctions.getPassword();


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
