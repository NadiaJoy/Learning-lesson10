package delivery;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.SetupFunctions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import  static com.codeborne.selenide.Selenide.*;
import  com.codeborne.selenide.Condition;


public class WebAppTest {
    public static String baseUrl;
    public static String openUrl;
    public static String username;
    public static String pwd;

    @BeforeAll
    public static void setup() {
        SetupFunctions setupFunctions = new SetupFunctions();

        username = setupFunctions.getUsername();
        pwd = setupFunctions.getPassword();

         baseUrl = setupFunctions.getBaseUrl();
         openUrl = baseUrl + ":3000/signing";
    }

    @Test
    public void incorrectLogin() {
        Configuration.holdBrowserOpen = true;
        //Configuration.browser = "edge";
        open(openUrl);

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

        open(openUrl);

        SelenideElement usernameInput = $(By.xpath("//input[@data-name='username-input']"));
        SelenideElement passwordInput = $(By.xpath("//input[@data-name='password-input']"));

        usernameInput.setValue(username);
        passwordInput.setValue(pwd);

        $(By.xpath("//button[@data-name=\"signIn-button\"]")).click();

        $(By.xpath("//button[@data-name=\"createOrder-button\"]")).shouldBe(Condition.visible);
    }
}
