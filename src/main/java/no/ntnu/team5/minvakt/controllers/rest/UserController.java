package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.model.MakeAvailableModel;
import no.ntnu.team5.minvakt.model.UserModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Boolean> registerAbsence(
            Verifier verifier,
            @PathVariable("username") String username,
            @PathVariable("shift") int shiftId) {

        verifier.ensure(isUser(username));

        return accessor.with(access -> {
            Shift shift = access.shift.getShiftFromId(shiftId);

            if (shift.isLocked()){
                System.out.println("Skiftet er låst og kan ikke endres. ShiftId: " + shift.getId());
                return ResponseEntity.unprocessableEntity().body(false);
            }

            if (!access.shift.addAbscence(shift, true)) {
                return ResponseEntity.unprocessableEntity().body(false);
            }

            access.shift.lockShift(shift);
            return ResponseEntity.ok().body(true);
        });
    }

    @Authorize
    @PostMapping("/{username}/available")
    public boolean makeAvailability(Verifier verify,
                                    @PathVariable("username") String username,
                                    @RequestBody MakeAvailableModel makeAvailableModel) {

        verify.ensure(isUser(username));

        System.out.println(makeAvailableModel);

        return accessor.with(access -> {
            return access.availability.makeAvailable(access.user.fromUsername(username), makeAvailableModel.getDateFrom(), makeAvailableModel.getDateTo());
        });
    }

    @Authorize
    @PostMapping("/{username}/unavailable")
    public boolean makeUnavailable(
            Verifier verify,
            @PathVariable("username") String username,
            @RequestBody MakeAvailableModel makeAvailableModel) {


        verify.ensure(Verifier.isUser(username));

        accessor.with(access -> {
            access.availability.makeUnavailable(access.user.fromUsername(username), makeAvailableModel.getDateFrom(), makeAvailableModel.getDateTo());
        });

        return true;
    }
    @Authorize
    @RequestMapping(value = "/{username}/removeabsence/{shift}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> removeAbsence(
            Verifier verifier,
            @PathVariable("username") String username,
            @PathVariable("shift") int shiftId) {

        verifier.ensure(isUser(username));

        return accessor.with(access -> {
            Shift shift = access.shift.getShiftFromId(shiftId);

            if (shift.isLocked()){
                System.out.println("Skiftet er låst og kan ikke endres. ShiftId: " + shift.getId());
                return ResponseEntity.unprocessableEntity().body(false);
            }

            if (!access.shift.removeAbscence(shift, false)) {
                return ResponseEntity.unprocessableEntity().body(false);
            }

            access.shift.lockShift(shift);
            return ResponseEntity.ok().body(true);
            
        });
    }
}
