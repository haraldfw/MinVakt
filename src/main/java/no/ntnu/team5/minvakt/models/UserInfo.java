package no.ntnu.team5.minvakt.models;

/**
 * Created by alan on 11/01/2017.
 */

public class UserInfo {
    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNr;
    private int employmentPercentage;

    public UserInfo(){
        //FIXME: Real defaults
        firstName = "UNKNOWN";
        lastName  = "UNKNOWN";
        phoneNr   = "UNKNOWN";
        email     = "UNKNOWN";
        employmentPercentage = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public int getEmploymentPercentage() {
        return employmentPercentage;
    }

    public void setEmploymentPercentage(int employmentPercentage) {
        this.employmentPercentage = employmentPercentage;
    }
}
