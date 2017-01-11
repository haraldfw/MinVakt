package no.ntnu.team5.minvakt.controllers;

import no.ntnu.team5.minvakt.dataaccess.UserAccess;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Kenan on 1/11/2017.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserAccess userAccess;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> verifyUser(LoginInfo lf) {
        User user = userAccess.fromUsername(lf.getUsername());
        boolean isVerified = PasswordUtil.verifyPassword(lf.getPassword(), user.getPasswordHash(), user.getSalt());

        if(isVerified) {

        }
        return null;
    }
}

class LoginInfo {

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