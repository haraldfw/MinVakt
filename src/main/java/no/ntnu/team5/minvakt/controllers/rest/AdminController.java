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
import no.ntnu.team5.minvakt.model.ShiftAssign;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.model.UserCreateResponse;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import no.ntnu.team5.minvakt.utils.EmailService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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
    private EmailService emailService;

    /**
     * Creates a user in the database.
     *
     * @param verifier Spring automatics
     * @param newUser  User object to create from
     * @return Response-entity with status and message
     */
    @Authorize
    @RequestMapping(value = "/create/user", method = RequestMethod.POST)
    public ResponseEntity<UserCreateResponse> createUser(Verifier verifier, @RequestBody NewUser newUser) {

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

        UserCreateResponse response = new UserCreateResponse();
        ResponseEntity.BodyBuilder builder = accessor.with(access -> {
            User user = new User();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPasswordHash(password_hash);
            user.setSalt(salt);
            user.setEmail(newUser.getEmail());
            user.setEmploymentPercentage(newUser.getEmploymentPercentage());
            user.setPhonenumber(newUser.getPhoneNr());
            user.setAddress(newUser.getAddress());
            user.setDateOfBirth(newUser.getDateOfBirth());
            user.setResetKey(resetKey);
            user.setResetKeyExpiry(resetKeyExpiry);

            Set<Competence> comps = new HashSet<>();
            newUser.getCompetences().forEach(s -> comps.add(access.competence.getFromName(s)));
            user.setCompetences(comps);

            Session session = access.getDb().getSession();
            Transaction tx = session.beginTransaction();

            try {
                session.save(user);

                emailService.userCreated(username, newUser.getEmail(), resetKey, resetKeyExpiry);
                response.setUserModel(access.user.toModel(user));
                tx.commit();

                return ResponseEntity.ok();
            } catch (ConstraintViolationException e) {
                tx.rollback();
                response.setErrorMsg("Epost er allerede i bruk.");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
            } catch (UnsupportedEncodingException e) {
                tx.rollback();
                response.setErrorMsg("Tegnsett ikke støttet.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
                response.setErrorMsg("Ukjent feil ved innlegging.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });

        return builder.body(response);
    }

    /**
     * Creates a shift in the database
     *
     * @param verify   Spring automagics
     * @param newShift Object to create shift from
     * @return ShiftModel representing the object just created
     */
    @Authorize
    @RequestMapping(value = "/create/shift", method = RequestMethod.POST)
    public ShiftModel createShift(Verifier verify, @RequestBody NewShift newShift) {
        verify.ensure(Verifier.hasRole(Constants.ADMIN));

        return accessor.with(access -> {
            Set<Competence> comps = new HashSet<>();
            newShift.getCompetences().forEach(s -> comps.add(access.competence.getFromName(s)));

            Shift shift = new Shift();
            shift.setStartTime(newShift.getStartTime());
            shift.setEndTime(newShift.getEndTime());
            shift.setAbsent(false);
            shift.setStandardHours((byte) ChronoUnit.HOURS.between(newShift.getStartTime().toInstant(), newShift.getEndTime().toInstant()));
            shift.setCompetences(comps);

            access.shift.save(shift);
            return access.shift.toModel(shift);
        });
    }

    /**
     * Assigns a new user for a shift
     *
     * @param verify      Spring automagics
     * @param shiftAssign ShiftAssign-object with it of shift and username of user to assign.
     */
    @Authorize
    @RequestMapping(value = "/assign/shift")
    public void addUserToShift(Verifier verify, @RequestBody ShiftAssign shiftAssign) {
        verify.ensure(Verifier.hasRole(Constants.ADMIN));

        accessor.with(access -> {
            Shift shift = access.shift.getShiftFromId(shiftAssign.getId());

            User oldShiftOwner = shift.getUser();

            String startTime = new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(shift.getStartTime());
            String endTime = new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(shift.getEndTime());
            User user = access.user.fromUsername(shiftAssign.getUsername());

            shift.setUser(user);

            access.shift.save(shift);

            String message = "Du har blitt tildelt følgende vakt:\n " + startTime + " til " + endTime;
            access.notification.generateMessageNotification(user, message);

            if (oldShiftOwner != null){
                message = "Du har blitt fjernet fra følgende vakt:\n " + startTime + " til " + endTime;
                access.notification.generateMessageNotification(oldShiftOwner, message);
            }
        });
    }

    /**
     * Sends a message with the given model.
     *
     * @param msg MessageModel to generate message(s) from.
     */
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

    /**
     * Creates a Competence in the database with the given model.
     *
     * @param verify        Springmagics
     * @param newCompetence Model to create from.
     */
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
