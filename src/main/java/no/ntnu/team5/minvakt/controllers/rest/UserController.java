package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.data.generation.UsernameGen;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.LoginResponse;
import no.ntnu.team5.minvakt.model.NewUser;
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

    @Autowired
    private UsernameGen usernameGen;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> create(@RequestBody NewUser info) {
        String salt = PasswordUtil.generateSalt();
        String password_hash = PasswordUtil.generatePasswordHash(info.getPassword(), salt);

        String firstName = info.getFirstName().trim();
        String lastName = info.getLastName().trim();

        User user = new User(
                usernameGen.generateUsername(firstName, lastName),
                firstName,
                lastName,
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
    public ResponseEntity<User> show(
            @CookieValue("access_token") String token,
            @PathVariable("username") String username) {

        JWT.isUser(token, username);

        return ResponseEntity.ok().body(userAccess.fromUsername(username));
    }
}
