package no.ntnu.team5.minvakt.models;

/**
 * Created by alan on 11/01/2017.
 */

public class LoginInfo {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}