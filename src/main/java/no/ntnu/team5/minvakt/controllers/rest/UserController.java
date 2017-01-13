package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.data.generation.UsernameGen;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.LoginResponse;
import no.ntnu.team5.minvakt.model.NewUser;
import no.ntnu.team5.minvakt.model.UserModel;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.auth.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserAccess userAccess;

    @Autowired
    private ShiftAccess shiftAccess;

    private UsernameGen usernameGen;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> create(
            @CookieValue("access_token") String token,
            @ModelAttribute(value = "newUser") NewUser newUser) {

        JWT.valid(token, JWT.hasRole("admin"));

        String salt = PasswordUtil.generateSalt();
        String password_hash = PasswordUtil.generatePasswordHash(newUser.getPassword(), salt);

        String firstName = newUser.getFirstName().trim();
        String lastName = newUser.getLastName().trim();

        User user = new User(
                usernameGen.generateUsername(firstName, lastName),
                firstName,
                lastName,
                password_hash,
                salt,
                newUser.getEmail(),
                newUser.getPhoneNr(),
                newUser.getEmploymentPercentage());

        userAccess.save(user);

        LoginResponse lr = new LoginResponse();
        lr.setSuccess(true);
        lr.setToken(JWT.generate(user));
        return ResponseEntity.ok().body(lr);
    }

    @RequestMapping(value = "/{username}")
    public UserModel show(
            @CookieValue("access_token") String token,
            @PathVariable("username") String username) {

        JWT.or(token, JWT.isUser(username), JWT.hasRole("admin"));

        return userAccess.toModel(userAccess.fromUsername(username));
    }
    @RequestMapping(value = "/{username}/nextshifts")
    public List<Shift> getNextShift (
            @CookieValue("access_token") String token,
            @PathVariable("username") String username) {

        JWT.valid(token, JWT.isUser(username));

        return shiftAccess.getShiftsForAUser(username);
    }
}
