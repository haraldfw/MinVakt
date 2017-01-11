package no.ntnu.team5.minvakt.models;

/**
 * Created by alan on 11/01/2017.
 */

public class LoginResponse {
    private boolean success;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
