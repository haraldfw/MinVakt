package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.*;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/api/shift")
public class ShiftController {
    @Autowired
    private AccessContextFactory accessor;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody ShiftModel shiftModel) {
        //FIXME: Should authorize as admin?
        int id = accessor.with(access -> {
            User user = access.user.fromID(shiftModel.getUserModel().getId());
            Shift shift = new Shift(user, shiftModel.getStartTime(), shiftModel.getEndTime(), shiftModel.getAbsent().byteValue(), shiftModel.getStandardHours().byteValue(), null);

            access.shift.save(shift);
            return shift.getId();
        });

        return ResponseEntity.ok().body(id);
    }

    @Authorize
    @RequestMapping("/{year}/{month}/{day}")
    public List<ShiftModel> getShifts(
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @PathVariable("day") int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();

        return accessor.with(access -> {
            return access.shift.getShiftsOnDate(date)
                    .stream()
                    .map(ShiftAccess::toModel)
                    .collect(Collectors.toList());
        });
    }

    @Authorize
    @PostMapping("/transfer")
    public ResponseEntity acceptTransfer(Verifier verifier,
                                         @RequestParam("accept") boolean accept,
                                         @RequestParam("shift_id") int shift_id,
                                         @RequestParam("user_id") int user_id,
                                         HttpServletRequest httpServletRequest) {

        accessor.with(access -> {

            System.out.println("Er jeg admin?");
            User me = access.user.fromUsername(verifier.claims.getSubject());
            System.out.println("Me: " + me.getUsername() + ", competences: " + me.getCompetences().toString());
            verifier.ensure(hasRole("Admin"));
            System.out.println("Ja det er jeg!");

            String actionURL = httpServletRequest.getRequestURI() + "?user_id=" + user_id + "&shift_id=" + shift_id;
            Notification notification = access.notification.fromActionURL(actionURL);
            if (notification == null) return;

            Shift shift = access.shift.getShiftFromId(shift_id);

            if (accept) {
                User newShiftOwner = access.user.fromID(user_id);
                User oldShiftOwner = shift.getUser();
                access.shift.transferOwnership(shift, newShiftOwner);
                String message = "Du har blitt tildelt følgende skift: " +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(newShiftOwner, message);
                message = "Følgende skift har blitt overtatt: " +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(oldShiftOwner, message);
            } else {
                User originalOwner = shift.getUser();
                if (originalOwner == null) {
                    System.out.println("Fant ingen tidligere skifteier.");
                    return;
                }

                String message = "Din forespørsel om bytte av følgende skift har blitt avslått av admin: " +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(originalOwner, message);
            }
            access.notification.closeNotification(notification);
        });
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @Authorize
    @PostMapping("/pass_notification")
    public void passTransferToAdmin(Verifier verifier,
                                    @RequestParam("accept") boolean accept,
                                    @RequestParam("shift_id") int shift_id,
                                    @RequestParam("user_id") int user_id,
                                    HttpServletRequest httpServletRequest) {

        accessor.with(access -> {

            String actionURL = httpServletRequest.getRequestURI() + "?shift_id=" + shift_id + "&user_id=" + user_id;
            Notification notification = access.notification.fromActionURL(actionURL);
            if (notification == null) {
                System.out.println("Fant ikke notifikasjon med den denne URL-en i databasen:\n"+actionURL);
                return;
            }
            verifier.ensure(or(isUser(notification.getUser().getUsername()), hasRole("Admin")));

            Shift shift = access.shift.getShiftFromId(shift_id);

            if (accept) {
                String message = "Bruker " + access.user.fromID(user_id).getUsername() +
                        " ønsker å ta over følgende skift fra " + shift.getUser().getUsername() +
                        ": " + shift.getStartTime() + " til " + shift.getEndTime() + ".";
                String nyActionURL = "/api/shift/transfer?user_id=" + user_id + "&shift_id=" + shift_id;
                access.notification.generateTransferNotification(access.competence.getFromName("Admin"), message, nyActionURL, shift);
            } else {
                User originalOwner = shift.getUser();
                if (originalOwner != null) {
                    String message = "Din forespørsel om bytte av følgende skift har blitt avslått: " +
                            shift.getStartTime() + " til " + shift.getEndTime() + ".";
                    access.notification.generateMessageNotification(originalOwner, message);
                }
            }
            access.notification.closeNotification(notification);
        });
    }
}

