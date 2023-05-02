package delivery;

import com.codeborne.selenide.Configuration;
import helpers.SetupFunctions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class WebRemoteTest {

    static  String baseUrl;
    @BeforeAll
    public static void setUpAll(){
      baseUrl = new SetupFunctions().getBaseUrlUI();

        Configuration.remote = "http://51.250.6.164:4444/wd/hub";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.browserVersion = "111.0";

        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.<String, Object>of(
                        "enableVNC", true,
                        "enableVideo", true
                ));

    }

    @BeforeEach
    public void setUp(){
        System.out.println("Trying to open browsers");
        open(baseUrl);
        System.out.println("browser Opened OK");
    }


    @AfterEach
    public void tearDown(){
        closeWebDriver();
    }

    @Test
    public void incorrectLoginAndCheckPopup(){

        $(By.id("username")).setValue("hello123");
        sleep( 2500 );

        $(By.id("password")).setValue("password");
        sleep( 2500 );

    }
    @Test
    public void incorrectLoginAndCheckPopupSecond(){

        $(By.id("username")).setValue("hello123");
        sleep( 2500 );

        $(By.id("password")).setValue("password");
        sleep( 2500 );

    }

}
