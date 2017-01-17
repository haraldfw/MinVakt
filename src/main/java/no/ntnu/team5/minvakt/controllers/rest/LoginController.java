package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.LoginInfo;
import no.ntnu.team5.minvakt.model.LoginResponse;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private AccessContextFactory accessor;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> verifyUser(HttpServletResponse response, @RequestBody LoginInfo loginInfo) {
        LoginResponse lr = accessor.with(access -> {
            User user = access.user.fromUsername(loginInfo.getUsername());
            return PasswordUtil.login(user, loginInfo.getPassword());
        });

        if (lr.getSuccess()) {
            Cookie cookie = new Cookie("access_token", lr.getToken());
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok().body(lr);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(lr);
        }
    }
}