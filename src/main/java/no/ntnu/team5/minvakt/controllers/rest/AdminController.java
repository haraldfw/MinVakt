package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.data.generation.UsernameGen;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.NewCompetence;
import no.ntnu.team5.minvakt.model.NewShift;
import no.ntnu.team5.minvakt.model.NewUser;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import no.ntnu.team5.minvakt.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.*;

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.hasRole;

/**
 * Created by Kenan on 1/20/2017.
 */

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AccessContextFactory accessor;

    @Autowired
    private UsernameGen usernameGen;

    @Autowired
    EmailService emailService;

    @Autowired
    UserAccess userAccess;

    @Authorize
    @RequestMapping(value = "/createuser", method = RequestMethod.POST)
    public void createUser(Verifier verifier, @RequestBody NewUser newUser) {

        verifier.ensure(hasRole(Constants.ADMIN));

        String salt = PasswordUtil.generateSalt();
        String password_hash = PasswordUtil.generatePasswordHash(PasswordUtil.generateSalt(), salt);

        String firstName = newUser.getFirstName().trim();
        String lastName = newUser.getLastName().trim();

        String resetKey = PasswordUtil.generateSalt();
        String username = usernameGen.generateUsername(firstName, lastName);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        Date resetKeyExpiry = c.getTime();

        Set<Competence> comps = new HashSet<>();
        newUser.getCompetences().forEach(s -> comps.add(accessor.with(accessContext -> {
            return accessContext.competence.getFromName(s);
        })));

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

            user.setCompetences(comps);
            user.setResetKey(resetKey);
            user.setResetKeyExpiry(resetKeyExpiry);

            access.user.save(user);
        });
    }

    @Authorize
    @RequestMapping("/createshift")
    public void createShift(Verifier verify, @RequestBody NewShift newShift) {
        verify.ensure(Verifier.hasRole(Constants.ADMIN));

        List<String> users = userAccess.getUsernames();

        Set<Competence> comps = new HashSet<>();
        newShift.getCompetences().forEach(s -> comps.add(accessor.with(accessContext -> {
            return accessContext.competence.getFromName(s);
        })));

        accessor.with(access -> {
            Shift shift = new Shift();


            shift.setUser(access.user.fromUsername(newShift.getUserModel().getUsername()));
            shift.setStartTime(newShift.getStartTime());
            shift.setEndTime(newShift.getEndTime());
            shift.setAbsent(newShift.getAbsent());
            shift.setStandardHours((byte) ChronoUnit.HOURS.between(newShift.getStartTime().toInstant(), newShift.getEndTime().toInstant()));
            shift.setCompetences(comps);

            access.shift.save(shift);
        });

    }

    @Authorize
    @RequestMapping("/createcompetence")
    public void createCompetence(Verifier verify, @RequestBody NewCompetence newCompetence) {
        verify.ensure(Verifier.hasRole(Constants.ADMIN));

        accessor.with(access -> {
            Competence competence = new Competence(
                    newCompetence.getName()
            );

            access.competence.save(competence);

        });
    }
}
