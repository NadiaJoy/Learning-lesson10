package dto;

public class CredentialsDto {
    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CredentialsDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CredentialsDto() {
    }
}
