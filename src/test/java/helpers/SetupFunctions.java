package helpers;

import com.google.gson.Gson;
import dto.CredentialsDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class SetupFunctions {
    //api
    String baseUrl;
    String baseUrlUI;
    String username;
    String password;

    //db
    String dbHost;
    String dbPort;
    String dbName;
    String dbUser;
    String dbPassword;


    public SetupFunctions() {
        try (InputStream input = new FileInputStream("settings.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            //api
            baseUrl = properties.getProperty("baseUrl");
            baseUrlUI = properties.getProperty("baseUrlUI");

            username = properties.getProperty("username");
            password = properties.getProperty("password");

            //db
             dbHost = properties.getProperty("dbHost");
             dbPort = properties.getProperty("dbPort");
             dbName = properties.getProperty("dbName");
             dbUser = properties.getProperty("dbUser");
             dbPassword = properties.getProperty("dbPassword");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getBaseUrlUI() {
        return baseUrlUI;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String createUser() {
        CredentialsDto user = new CredentialsDto(username, password);
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getToken() {

        return given().
                header("Content-type", "application/json").
                log().
                all().
                body(createUser()).
                when().
                post(  baseUrl + "/login/student").
                then().
                log().
                all().
                extract().
                response().
                asString();
    }


}
