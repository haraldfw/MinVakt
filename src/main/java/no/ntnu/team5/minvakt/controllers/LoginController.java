package no.ntnu.team5.minvakt.controllers;

import no.ntnu.team5.minvakt.dataaccess.UserAccess;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.models.LoginInfo;
import no.ntnu.team5.minvakt.models.LoginResponse;
import no.ntnu.team5.minvakt.security.JWT;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<LoginResponse> verifyUser(@RequestBody LoginInfo lf) {
        User user = userAccess.fromUsername(lf.getUsername());

        boolean isVerified = PasswordUtil.verifyPassword(lf.getPassword(), user.getPasswordHash(), user.getSalt());

        LoginResponse lr = new LoginResponse();
        if(isVerified) {
            lr.setSuccess(true);
            lr.setToken(JWT.generate(user));
            return ResponseEntity.ok().body(lr);
        } else {
            lr.setSuccess(false);
            return ResponseEntity.badRequest().body(lr);
        }
    }
}