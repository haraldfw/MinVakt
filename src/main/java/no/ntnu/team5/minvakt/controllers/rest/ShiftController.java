package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.model.UserModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody ShiftModel shiftModel) {
        //FIXME: Should authorize as admin?
        int id = accessor.with(access -> {
            User user = access.user.fromID(shiftModel.getUserModel().getId());
            Shift shift = new Shift(user, shiftModel.getStartTime(), shiftModel.getEndTime(), shiftModel.getAbsent(), shiftModel.getStandardHours().byteValue(), null);

            access.shift.save(shift);
            return shift.getId();
        });

        return ResponseEntity.ok().body(id);
    }

    /**
     * Get shifts for the current week for a given user
     * @return a list of shifts
     */
    //@Authorize("/") //FIXME: will not authorize
    @RequestMapping("/{user}/week") //TODO: gjør sånn at man går til "/week" og henter for bestemt bruker
    public List<ShiftModel> getShiftsCurrentWeek(@PathVariable("user") String username) {
        Calendar cal = Calendar.getInstance();
        Date startWeek = cal.getTime();

        return accessor.with(access -> {
           return access.shift.getAllCurrentWeekForUser(startWeek, username)
                   .stream()
                   .map(access.shift::toModel)
                   .collect(Collectors.toList());
        });
    }

    /**
     * Get shifts for a week with a given start date
     * @param username the user that we gets shifts for
     * @param year startYear
     * @param month startMonth
     * @param day startDay
     * @return a list of shifts for a week for a user with a given start date
     */
    //@Authorize("/") //FIXME: will not authorize
    //FIXME: If the user sends a lot of requests in a short period the server will crash.
    @RequestMapping("/{user}/{year}/{month}/{day}/week")
    public List<ShiftModel> getShiftsWeek(@PathVariable("user") String username,
                                              @PathVariable("year") int year,
                                              @PathVariable("month") int month,
                                              @PathVariable("day") int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day); //TODO: check if is correct with other dates than rigth now
        calendar.set(Calendar.HOUR, 0); //TODO: trenger vi hour, minute, second, millisecond
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date fromDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date toDate = calendar.getTime();

        return accessor.with(access -> {
           return access.shift.getShiftsFromDateToDateForUser(fromDate, toDate, username)
                   .stream()
                   .map(access.shift::toModel)
                   .collect(Collectors.toList());
        });
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
                    .map(access.shift::toModel)
                    .collect(Collectors.toList());
        });
    }

    @RequestMapping("/get_available_users_for_shift")
    public List<UserModel> getAvailableUsers(@RequestParam("shift_id") int shift_id){
        return accessor.with(access -> {
            Shift shift = access.shift.getShiftFromId(shift_id);
            Date fromDate = shift.getStartTime(), toDate = shift.getEndTime();
            List<User>  users = access.availability.listAvailableUsers(fromDate, toDate);
            return access.user.toModel(users);
        });
    }

    @Authorize
    @PostMapping("/transfer")
    public ResponseEntity acceptTransfer(Verifier verifier,
                                         @RequestParam("accept") boolean accept,
                                         @RequestParam("shift_id") int shift_id,
                                         @RequestParam("user_id") int user_id,
                                         HttpServletRequest httpServletRequest) {
        verifier.ensure(hasRole(Constants.ADMIN));
        accessor.with(access -> {

            String actionUrl = httpServletRequest.getRequestURI() + "?shift_id=" + shift_id + "&user_id=" + user_id;
            Notification notification = access.notification.fromActionURL(actionUrl);
            if (notification == null){
                System.out.println("Fant ikke notifikasjon med den denne URL-en i databasen:\n" + actionUrl);
                return;
            }
            Shift shift = access.shift.getShiftFromId(shift_id);
            if (accept) {
                User newShiftOwner = access.user.fromID(user_id);
                User oldShiftOwner = shift.getUser();
                access.shift.transferOwnership(shift, newShiftOwner);
                String message = "Du har blitt tildelt følgende vakt: " +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(newShiftOwner, message);
                message = "Følgende vakt har blitt overtatt av " + newShiftOwner.getFirstName() + " " + newShiftOwner.getLastName() + ": \n" +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(oldShiftOwner, message);
            } else {
                User originalOwner = shift.getUser();
                if (originalOwner == null) {
                    System.out.println("Fant ingen tidligere skifteier.");
                    return;
                }
                String message = "Din forespørsel om bytte av følgende vakt har blitt avslått av admin: " +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(originalOwner, message);
            }
            access.notification.closeNotification(notification);
        });
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @Authorize
    @PostMapping("/pass_notification_to_admin")
    public void passTransferToAdmin(Verifier verifier,
                                    @RequestParam("shift_id") int shift_id,
                                    @RequestParam("user_id") int user_id,
                                    @RequestParam("accept") boolean accept,
                                    HttpServletRequest httpServletRequest) {
        accessor.with(access -> {
            String actionUrl = httpServletRequest.getRequestURI() + "?shift_id=" + shift_id + "&user_id=" + user_id;
            Notification notification = access.notification.fromActionURL(actionUrl);
            if (notification == null) {
                System.out.println("Fant ikke notifikasjon med den denne URL-en i databasen:\n" + actionUrl);
                return;
            }
            verifier.ensure(or(isUser(notification.getUser().getUsername()), hasRole(Constants.ADMIN)));

            Shift shift = access.shift.getShiftFromId(shift_id);

            if (accept) {
                String message = "Bruker " + access.user.fromID(user_id).getUsername() +
                        " ønsker å ta over følgende vakt fra " + shift.getUser().getUsername() +
                        ": " + shift.getStartTime() + " til " + shift.getEndTime() + ".";
                String nyActionURL = "/api/shift/transfer?shift_id=" + shift_id + "&user_id=" + user_id;
                access.notification.generateTransferNotification(access.competence.getFromName("Admin"), message, nyActionURL);
            } else {
                User originalOwner = shift.getUser();
                if (originalOwner != null) {
                    String message = "Din forespørsel om bytte av følgende vakt har blitt avslått: " +
                            shift.getStartTime() + " til " + shift.getEndTime() + ".";
                    access.notification.generateMessageNotification(originalOwner, message);
                }
            }
            access.notification.closeNotification(notification);
        });
    }

    @Authorize
    @PostMapping("/release_user_from_shift")
    public void releaseUserFromShift(Verifier verifier,
                                     @RequestParam("shift_id") int shift_id,
                                     @RequestParam("accept") boolean accept,
                                     HttpServletRequest httpServletRequest){
        verifier.ensure(hasRole(Constants.ADMIN));
        accessor.with(access -> {
            String actionUrl = httpServletRequest.getRequestURI() + "?shift_id" + shift_id;
            Notification notification = access.notification.fromActionURL(actionUrl);
            if (notification == null){
                System.out.println("Fant ikke notifikasjon med den denne URL-en i databasen:\n" + actionUrl);
                return;
            }
            Shift shift = access.shift.getShiftFromId(shift_id);
            if (accept){
                User user = shift.getUser();
                access.shift.transferOwnership(shift, null);
                String message = "Du har blitt frigjort fra følgende vakt:\n " +
                        shift.getStartTime() + " til " + shift.getEndTime();
                access.notification.generateMessageNotification(user, message);
            }else {
                User user = shift.getUser();
                if (user == null) {
                    System.out.println("Fant ingen tidligere skifteier.");
                    return;
                }
                String message = "Din forespørsel om å bli frigjort fra følgende vakt har blitt avslått av admin: " +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(user, message);
            }
            access.notification.closeNotification(notification);
        });
    }
}

