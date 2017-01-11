package no.ntnu.team5.minvakt.controllers;

import no.ntnu.team5.minvakt.dataaccess.UserAccess;
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

    }
}

class LoginInfo {
    private String userName;
    private
}