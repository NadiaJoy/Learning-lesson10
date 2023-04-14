package delivery;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.SetupFunctions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import  static com.codeborne.selenide.Selenide.*;
import  com.codeborne.selenide.Condition;


public class WebAppTest {

    @Test
    public void incorrectLogin() {
        Configuration.holdBrowserOpen = true;
        //Configuration.browser = "edge";
        open("http://51.250.6.164:3000/signin");

        SelenideElement usernameInput = $(By.xpath("//input[@data-name='username-input']"));
        SelenideElement passwordInput = $(By.xpath("//input[@data-name='password-input']"));

        usernameInput.setValue("hello");
        passwordInput.setValue("12345678");

        $(By.xpath("//button[@data-name=\"signIn-button\"]")).click();

        $(By.xpath("//div[@data-name=\"authorizationError-popup\"]")).shouldBe(Condition.exist);

    }

    @Test
    public void successfulLogin() {
        SetupFunctions setupFunctions = new SetupFunctions();

        String username = setupFunctions.getUsername();
        String pwd = setupFunctions.getPassword();

        Configuration.holdBrowserOpen = true;

        open("http://51.250.6.164:3000/signin");

        SelenideElement usernameInput = $(By.xpath("//input[@data-name='username-input']"));
        SelenideElement passwordInput = $(By.xpath("//input[@data-name='password-input']"));

        usernameInput.setValue(username);
        passwordInput.setValue(pwd);

        $(By.xpath("//button[@data-name=\"signIn-button\"]")).click();

        $(By.xpath("//button[@data-name=\"createOrder-button\"]")).shouldBe(Condition.visible);
    }
}
