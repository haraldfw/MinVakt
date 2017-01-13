package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.LoginInfo;
import no.ntnu.team5.minvakt.model.LoginResponse;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.auth.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Kenan on 1/11/2017.
 */
@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    UserAccess userAccess;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> verifyUser(HttpServletResponse response, @ModelAttribute LoginInfo loginInfo) {
        System.out.println(loginInfo.toString());

        User user = userAccess.fromUsername(loginInfo.getUsername());

        boolean isVerified = PasswordUtil.verifyPassword(loginInfo.getPassword(), user.getPasswordHash(), user.getSalt());

        LoginResponse lr = new LoginResponse();
        if (isVerified) {
            lr.setSuccess(true);
            String token = JWT.generate(user);
            lr.setToken(token);
            response.addCookie(new Cookie("access_token", token));
            return ResponseEntity.ok().body(lr);
        } else {
            lr.setSuccess(false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(lr);
        }
    }

//    @PostMapping("/login")
//    private ResponseEntity<LoginResponse> verifyUser(HttpServletResponse response, @ModelAttribute LoginInfo loginInfo) {
//        System.out.println(loginInfo.toString());
//
//        User user = userAccess.fromUsername(loginInfo.getUsername());
//
//        boolean isVerified = PasswordUtil.verifyPassword(loginInfo.getPassword(), user.getPasswordHash(), user.getSalt());
//
//        LoginResponse lr = new LoginResponse();
//        if (isVerified) {
//            lr.setSuccess(true);
//            String token = JWT.generate(user);
//            lr.setToken(token);
//            response.addCookie(new Cookie("access_token", token));
//            return ResponseEntity.ok().body(lr);
//        } else {
//            lr.setSuccess(false);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(lr);
//        }
//    }
}