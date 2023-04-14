package delivery;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.SetupFunctions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import  static com.codeborne.selenide.Selenide.*;
import  com.codeborne.selenide.Condition;


public class WebAppTest {
    public  String baseUrl;
    public  String username;
    public  String pwd;

    @BeforeAll
    public void preSetup() {
        SetupFunctions setupFunctions = new SetupFunctions();
        baseUrl = setupFunctions.getBaseUrl();

        username = setupFunctions.getUsername();
        pwd = setupFunctions.getPassword();
    }
    @BeforeEach
    public void setup() {
         open(baseUrl);
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }


    @Test
    public void incorrectLogin() {
        Configuration.holdBrowserOpen = true;

        SelenideElement usernameInput = $(By.xpath("//input[@data-name='username-input']"));
        SelenideElement passwordInput = $(By.xpath("//input[@data-name='password-input']"));

        usernameInput.setValue("hello");
        passwordInput.setValue("12345678");

        $(By.xpath("//button[@data-name=\"signIn-button\"]")).click();

        $(By.xpath("//div[@data-name=\"authorizationError-popup\"]")).shouldBe(Condition.exist);

    }

    @Test
    public void successfulLogin() {

        Configuration.holdBrowserOpen = true;

        SelenideElement usernameInput = $(By.xpath("//input[@data-name='username-input']"));
        SelenideElement passwordInput = $(By.xpath("//input[@data-name='password-input']"));

        usernameInput.setValue(username);
        passwordInput.setValue(pwd);

        $(By.xpath("//button[@data-name=\"signIn-button\"]")).click();

        $(By.xpath("//button[@data-name=\"createOrder-button\"]")).shouldBe(Condition.visible);
    }
}
