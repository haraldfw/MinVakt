package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.generation.UsernameGen;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.MakeAvailableModel;
import no.ntnu.team5.minvakt.model.NewUser;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.model.UserModel;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import no.ntnu.team5.minvakt.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.hasRole;
import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.isUser;
import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.or;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private AccessContextFactory accessor;

    @Autowired
    private UsernameGen usernameGen;

    @Autowired
    EmailService emailService;

    @Authorize
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(Verifier verifier, @ModelAttribute("newUser") NewUser newUser) {
        verifier.ensure(hasRole(Constants.ADMIN));

        String salt = PasswordUtil.generateSalt();
        String password_hash = PasswordUtil.generatePasswordHash(newUser.getPassword(), salt);

        String firstName = newUser.getFirstName().trim();
        String lastName = newUser.getLastName().trim();

        String resetKey = PasswordUtil.generateSalt();
        String username = usernameGen.generateUsername(firstName, lastName);

        accessor.with(access -> {
            User user = new User(
                    username,
                    firstName,
                    lastName,
                    password_hash,
                    salt,
                    newUser.getEmail(),
                    newUser.getPhoneNr(),
                    newUser.getEmploymentPercentage());

            Set<Competence> comps = access.competence.getFromNames(newUser.getCompetences());
            user.setCompetences(comps);

            user.setResetKey(resetKey);

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            user.setResetKeyExpiry(c.getTime());

            access.user.save(user);
        });

        // TODO make sure creation is successful before sending email
        // TODO email templating


        try {
            String encodedKey = URLEncoder.encode(resetKey, "UTF-8");
            emailService.sendEmail(
                    newUser.getEmail(),
                    "User has been created for you in MinVakt",
                    "http://localhost:8080/password/reset?username=" +
                            username + "&resetkey=" + encodedKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Authorize
    @RequestMapping(value = "/available", method = RequestMethod.POST)
    public boolean makeAvailability(
            Verifier verifier,
            @RequestBody MakeAvailableModel mam,
            @PathVariable String username) {

        verifier.ensure(Verifier.isUser(username));

        return accessor.with(access -> {
            return access.availability.makeAvailable(access.user.fromUsername(username), mam.getDateFrom(), mam.getDateTo());
        });
    }
    @Authorize
    @RequestMapping(value = "/{username}")
    public UserModel show(Verifier verifier, @PathVariable("username") String username) {
        verifier.ensure(or(isUser(username), hasRole("admin")));

        return accessor.with(access -> {
            return access.user.toModel(access.user.fromUsername(username));
        });
    }

    @Authorize
    @RequestMapping(value = "/{username}/registerabsence/{shift}", method = RequestMethod.PUT)
    public boolean registerAbsence(
            Verifier verifier,
            @PathVariable("username") String username,
            @PathVariable("shift") int shiftId) {

        verifier.ensure(isUser(username));

        accessor.with(access -> {
            access.shift.addAbscence(access.shift.getShiftFromId(shiftId), (byte) 1);
        });

        return true;
    }

    @Authorize
    @PostMapping("/{username}/unavailable")
    public boolean makeUnavailable(
            Verifier verify,
            @PathVariable("username") String username,
            @RequestBody MakeAvailableModel mam) {


        verify.ensure(Verifier.isUser(username));

        accessor.with(access -> {
            access.availability.makeUnavailable(access.user.fromUsername(username), mam.getDateFrom(), mam.getDateTo());
        });

        return true;
    }
}
