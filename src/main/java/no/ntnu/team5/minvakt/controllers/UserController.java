package no.ntnu.team5.minvakt.controllers;

import no.ntnu.team5.minvakt.dataaccess.UserAccess;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.models.LoginResponse;
import no.ntnu.team5.minvakt.models.UserInfo;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserAccess userAccess;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> create(@RequestBody UserInfo info) {
        String salt = PasswordUtil.generateSalt();
        String password_hash = PasswordUtil.generatePasswordHash(info.getPassword(), salt);

        User user = new User(
                info.getUsername(),
                info.getFirstName(),
                info.getLastName(),
                password_hash,
                salt,
                info.getEmail(),
                info.getPhoneNr(),
                info.getEmploymentPercentage());

        userAccess.save(user);

        LoginResponse lr = new LoginResponse();
        lr.setSuccess(true);
        lr.setToken(JWT.generate(user));
        return ResponseEntity.ok().body(lr);
    }

    @RequestMapping(value = "/{username}")
    public ResponseEntity<String> show(
            @CookieValue("access_token") String token,
            @PathVariable("username") String username) {

        JWT.hasAccess(token, (claims, user) -> claims.has("sub", username));

        return ResponseEntity.ok().body("YAY!");
    }
}
