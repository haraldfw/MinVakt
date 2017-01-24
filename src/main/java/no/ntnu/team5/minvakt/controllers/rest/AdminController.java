package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.generation.UsernameGen;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.MessageModel;
import no.ntnu.team5.minvakt.model.NewCompetence;
import no.ntnu.team5.minvakt.model.NewShift;
import no.ntnu.team5.minvakt.model.NewUser;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import no.ntnu.team5.minvakt.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Authorize
    @RequestMapping(value = "/create/user", method = RequestMethod.POST)
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

        try {
            String encodedKey = URLEncoder.encode(resetKey, "UTF-8");
            String subject = "User has been created for you in MinVakt";
            String link = "http://localhost:8080/password/reset?username=" +
                    username + "&resetkey=" + encodedKey;
            String expiry = new SimpleDateFormat("yyyy-M-d kk:mm").format(resetKeyExpiry);

            Map<String, String> vars = new HashMap<>();
            vars.put("link", link);
            vars.put("expiry", expiry);
            vars.put("username", username);

            emailService.sendEmail(
                    newUser.getEmail(), subject, "email/user_created", vars);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Authorize
    @RequestMapping(value = "/create/shift", method = RequestMethod.POST)
    public void createShift(Verifier verify, @RequestBody NewShift newShift) {
        verify.ensure(Verifier.hasRole(Constants.ADMIN));

        accessor.with(access -> {
            List<String> users = access.user.getUsernames();

            Set<Competence> comps = new HashSet<>();
            newShift.getCompetences().forEach(s -> comps.add(access.competence.getFromName(s)));

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
    @PostMapping("/message")
    public void sendMessage(@RequestBody MessageModel msg) {
        System.out.println(msg.toString());
        Set<String> usernames = new HashSet<>();
        usernames.addAll(msg.getUserRecs());

        accessor.with(accessContext -> {
            msg.getCompRecs().forEach(comp -> {
                Competence competence = accessContext.competence.getFromName(comp);
                System.out.println(competence);
                System.out.println(comp);
                competence.getUsers().stream().map(User::getUsername).forEach(usernames::add);
            });

            usernames.forEach(username -> {
                User user = accessContext.user.fromUsername(username);
                accessContext.notification.generateMessageNotification(user, msg.getMessage());
            });
        });
    }

    @Authorize
    @RequestMapping("/create/competence")
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
