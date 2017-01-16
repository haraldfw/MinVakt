package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.security.auth.JWT;
import no.ntnu.team5.minvakt.security.auth.verify.Verification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import no.ntnu.team5.minvakt.data.access.AvailabilityAccess;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.data.access.UserAccess;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;

import no.ntnu.team5.minvakt.data.generation.UsernameGen;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;

import no.ntnu.team5.minvakt.model.LoginResponse;
import no.ntnu.team5.minvakt.model.MakeAvailableModel;


import no.ntnu.team5.minvakt.model.NewUser;
import no.ntnu.team5.minvakt.model.UserModel;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.*;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private AccessContextFactory accessor;

    @Autowired

    private ShiftAccess shiftAccess;

    @Autowired
    private AvailabilityAccess availabilityAccess;

    private UsernameGen usernameGen;

    @Authorize
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(Verifier verifier, @ModelAttribute("newUser") NewUser newUser) {
        verifier.ensure(hasRole("admin"));

        String salt = PasswordUtil.generateSalt();
        String password_hash = PasswordUtil.generatePasswordHash(newUser.getPassword(), salt);

        String firstName = newUser.getFirstName().trim();
        String lastName = newUser.getLastName().trim();

        accessor.with(access -> {
            User user = new User(
                    usernameGen.generateUsername(firstName, lastName),
                    firstName,
                    lastName,
                    password_hash,
                    salt,
                    newUser.getEmail(),
                    newUser.getPhoneNr(),
                    newUser.getEmploymentPercentage());

            Set<Competence> comps = access.competence.getFromNames(newUser.getCompetences());
            System.out.println("comps: " + comps.size());
            user.setCompetences(comps); //FIXME

            access.user.save(user);
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
    @RequestMapping("/{username}/nextshifts")
    public List<Shift> getNextShift(Verifier verifier, @PathVariable("username") String username) {
        verifier.ensure(isUser(username));

        return accessor.with(access -> {
            return access.shift.getShiftsForAUser(username);
        });
    }

    @Authorize
    @RequestMapping(value = "/{username}/registerabscence/{shift}", method = RequestMethod.PUT)
    public boolean registerAbsence (
            Verifier verifier,
            @PathVariable("username") String username,
            @PathVariable("shift") int shiftId ) {

        verifier.ensure(isUser(username));

        accessor.with(access -> {
            access.shift.addAbscence(access.shift.getShiftFromId(shiftId), (byte) 1);
            return null;
        });

        return true;
    }
    @Authorize
    @RequestMapping(value = "/{username}/available", method = RequestMethod.POST)
    public boolean makeAvailability (
            Verifier verify,
            @PathVariable("username") String username,
            @RequestBody MakeAvailableModel mam) {

        verify.ensure(Verifier.isUser(username));
        availabilityAccess.makeAvailable(mam.getDateFrom(), mam.getDateTo());

        return true;
    }
    public boolean makeUnavailable (
            Verifier verify,
            @PathVariable("username") String username,
            @RequestBody MakeAvailableModel mam) {


        verify.ensure(Verifier.isUser(username));
        return true;
    }
}
