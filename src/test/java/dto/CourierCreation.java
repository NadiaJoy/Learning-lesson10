package dto;

public class CourierCreation {

    int id;
    String login;
    String password;
    String name;

    public CourierCreation(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }
}
