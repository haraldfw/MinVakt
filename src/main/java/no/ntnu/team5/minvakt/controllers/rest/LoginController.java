package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.Access;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.LoginInfo;
import no.ntnu.team5.minvakt.model.LoginResponse;
import no.ntnu.team5.minvakt.security.PasswordUtil;
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
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> verifyUser(HttpServletResponse response, @ModelAttribute LoginInfo loginInfo) {
        LoginResponse lr = Access.with(access -> {
            User user = access.user.fromUsername(loginInfo.getUsername());
            return PasswordUtil.login(user, loginInfo.getPassword());
        });

        if (lr.getSuccess()) {
            response.addCookie(new Cookie("access_token", lr.getToken()));
            return ResponseEntity.ok().body(lr);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(lr);
        }
    }
}