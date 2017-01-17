package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.ActionURLAccess;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.data.generation.NotificationGen;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.hasRole;
import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.isUser;
import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.or;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/api/shift")
public class ShiftController {
    @Autowired
    private AccessContextFactory accessor;

    @Autowired
    ActionURLAccess actionURLAccess;

    @Autowired
    ShiftAccess shiftAccess;

    @Autowired
    UserAccess userAccess;

    @Autowired
    NotificationGen notificationGen;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody ShiftModel shiftModel) {
        //FIXME: Should authorize as admin?
        int id = accessor.with(access -> {
            User user = access.user.fromID(shiftModel.getUserId());
            Shift shift = new Shift(user, shiftModel.getStartTime(), shiftModel.getEndTime(), shiftModel.getAbsent().byteValue(), shiftModel.getStandardHours().byteValue(), null);

            access.shift.save(shift);
            return shift.getId();
        });

        return ResponseEntity.ok().body(id);
    }

    @Authorize
    @RequestMapping("/{year}/{month}/{day}")
    public List<Shift> getShifts(
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @PathVariable("day") int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();

        return accessor.with(access -> {
            return access.shift.getShiftsOnDate(date);
        });
    }

    @Authorize
    @PostMapping("/transfer")
    public void acceptTransfer(Verifier verifier,
                                   @RequestParam("accept") boolean accept,
                                   @RequestParam("shift_id") int shift_id,
                                   @RequestParam("user_id") int user_id,
                                   HttpServletRequest httpServletRequest) {

        verifier.ensure(hasRole("Admin"));

        String actionURL = httpServletRequest.getRequestURI() + "?accept="+accept+"&shift_id="+shift_id;
        Notification notification = actionURLAccess.fromActionURL(actionURL);
        if (notification == null)return;

        Shift shift = shiftAccess.getShiftFromId(shift_id);

        if (accept){
            User newShiftOwner = userAccess.fromID(user_id);
            User oldShiftOwner = shift.getUser();
            shiftAccess.transferOwnership(shift, newShiftOwner);
            String message = "Du har blitt tildelt følgende skift: " +
                    shift.getStartTime() + " til " + shift.getEndTime() + ".";
            notificationGen.generateMessageNotification(newShiftOwner, message);
            message = "Følgende skift har blitt overtatt: " +
                    shift.getStartTime() + " til " + shift.getEndTime() + ".";
            notificationGen.generateMessageNotification(oldShiftOwner, message);
        }else {
            User originalOwner = shift.getUser();
            if (originalOwner == null)return;

            String message = "Din forespørsel om bytte av følgende skift har blitt avslått: " +
                    shift.getStartTime() + " til " + shift.getEndTime() + ".";
            notificationGen.generateMessageNotification(originalOwner, message);
        }
    }

    @Authorize
    @PostMapping("/pass_notification")
    public void passTransferToAdmin(Verifier verifier,
                                    @RequestParam("accept") boolean accept,
                                    @RequestParam("shift_id") int shift_id,
                                    @RequestParam("user_id") int user_id,
                                    HttpServletRequest httpServletRequest){

        String actionURL = httpServletRequest.getRequestURI() + "?accept="+accept+"&shift_id="+shift_id;
        Notification notification = actionURLAccess.fromActionURL(actionURL);
        if (notification ==  null)return;
        verifier.ensure(or(isUser(notification.getUser().getUsername()), hasRole("Admin")));

        Shift shift = shiftAccess.getShiftFromId(shift_id);

        if (accept){
            String message = "Bruker " + userAccess.fromID(user_id).getUsername() +
                    " ønsker å ta over følgende skift fra " + shift.getUser().getUsername() +
                    ": " + shift.getStartTime() + " til " + shift.getEndTime() + ".";
            String nyActionURL = "/api/shift/transfer?shift_id="+user_id+"&shift_id"+shift_id;
            notificationGen.generateTransferNotification(userAccess.fromUsername("admin"), message, nyActionURL, shift_id);
        }else {
            User originalOwner = shift.getUser();
            if (originalOwner == null)return;

            String message = "Din forespørsel om bytte av følgende skift har blitt avslått: " +
                    shift.getStartTime() + " til " + shift.getEndTime() + ".";
            notificationGen.generateMessageNotification(originalOwner, message);
        }
    }
}

