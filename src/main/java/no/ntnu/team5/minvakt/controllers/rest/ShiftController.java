package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.PunchInOutModel;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.model.UserModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import no.ntnu.team5.minvakt.utils.EmailService;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

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
    private EmailService emailService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody ShiftModel shiftModel) {
        //FIXME: Should authorize as admin?
        int id = accessor.with(access -> {
            User user = access.user.fromID(shiftModel.getUserModel().getId());
            Shift shift = new Shift(user, shiftModel.getStartTime(), shiftModel.getEndTime(), shiftModel.getAbsent(), shiftModel.getLocked(), shiftModel.getStandardHours().byteValue(), null);

            access.shift.save(shift);
            return shift.getId();
        });

        return ResponseEntity.ok().body(id);
    }

    /**
     * Get shifts for the current week for a given user
     *
     * @return a list of shifts
     */
    //@Authorize("/") //FIXME: will not authorize
    @RequestMapping("/{user}/week")
    //TODO: gjør sånn at man går til "/week" og henter for bestemt bruker
    public List<ShiftModel> getShiftsCurrentWeek(@PathVariable("user") String username) {
        return accessor.with(access -> {
            return access.shift.getAllCurrentWeekForUser(new Date(), username)
                    .stream()
                    .map(access.shift::toModel)
                    .collect(Collectors.toList());
        });
    }

    /**
     * Get shifts for a week with a given start date
     *
     * @param username the user that we gets shifts for
     * @param year     startYear
     * @param month    startMonth
     * @param day      startDay
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
        calendar.set(Calendar.HOUR_OF_DAY, 0); //TODO: trenger vi hour, minute, second, millisecond
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

    @Authorize
    @PostMapping("/settime")
    public ResponseEntity setTime(@RequestBody PunchInOutModel punchInOutModel) {
        if (punchInOutModel == null || punchInOutModel.getShiftId() == null) {
            return ResponseEntity.badRequest().body("Missing body or missing field shift_id");
        }

        return accessor.with(accessContext -> {
            Shift shift = accessContext.shift.getShiftFromId(punchInOutModel.getShiftId());

            Date startTime = punchInOutModel.getStartTime();
            Date endTime = punchInOutModel.getEndTime();

            if (startTime != null && endTime != null) {
                if (startTime.after(endTime)) {
                    return ResponseEntity.badRequest().body("start_time is after end_time");
                }
                shift.setStartTime(startTime);
                shift.setEndTime(endTime);
            } else if (startTime != null) {
                if (startTime.after(shift.getEndTime())) {
                    return ResponseEntity
                            .badRequest()
                            .body("start_time is after shift's endTime");
                }
                shift.setStartTime(startTime);
            } else if (endTime != null) {
                if (endTime.before(shift.getStartTime())) {
                    return ResponseEntity
                            .badRequest()
                            .body("end_time is before shift's startTime");
                }
                shift.setEndTime(endTime);
            } else {
                // both time-objects are null or one is invalid
                return ResponseEntity
                        .badRequest()
                        .body("Shift-object unchanged due to both time-objects being null.");
            }
            accessContext.shift.save(shift);
            return ResponseEntity
                    .ok()
                    .body("Shift-object updated");
        });
    }

    @Authorize
    @RequestMapping("/get_available_users_for_shift")
    public List<UserModel> getAvailableUsers(@RequestParam("shift_id") int shift_id) {
        return accessor.with(access -> {
            Shift shift = access.shift.getShiftFromId(shift_id);
            Date fromDate = shift.getStartTime(), toDate = shift.getEndTime();
            List<User> users = access.availability.listAvailableUsers(fromDate, toDate);
            return access.user.toModel(users);
        });
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
                    access.shift.unlockShift(shift);
                }
            }
            access.notification.closeNotification(notification);
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
            if (notification == null) {
                System.out.println("Fant ikke notifikasjon med den denne URL-en i databasen:\n" + actionUrl);
                return;
            } else if (notification.getClosed()) {
                System.out.println("Denne notifikasjonen har allerede blitt behandlet!");
                return;
            }
            Shift shift = access.shift.getShiftFromId(shift_id);
            if (accept) {
                User newShiftOwner = access.user.fromID(user_id);
                User oldShiftOwner = shift.getUser();
                access.shift.transferOwnership(shift, newShiftOwner);

                Map<String, String> vars = new HashMap<>();
                vars.put("date_start", emailService.formatDate(shift.getStartTime()));
                vars.put("date_end", emailService.formatDate(shift.getEndTime()));
                vars.put("new_owner_full_name", newShiftOwner.getFirstName() + " " + newShiftOwner.getLastName());
                vars.put("old_owner_full_name", oldShiftOwner.getFirstName() + " " + oldShiftOwner.getLastName());

                String message = "Du har blitt tildelt følgende vakt: " +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(newShiftOwner, message);

                emailService.sendEmail(newShiftOwner.getEmail(),
                        "Du har overtatt en vakt",
                        "email/shift_transfer_new_owner", vars);


                message = "Følgende vakt har blitt overtatt av " + newShiftOwner.getFirstName() + " " + newShiftOwner.getLastName() + ": \n" +
                        shift.getStartTime() + " til " + shift.getEndTime() + ".";
                access.notification.generateMessageNotification(oldShiftOwner, message);

                emailService.sendEmail(oldShiftOwner.getEmail(),
                        "Vakt er overtatt av " + newShiftOwner.getUsername(),
                        "email/shift_transfer_old_owner", vars);
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
            access.shift.unlockShift(shift);
        });
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @Authorize
    @PostMapping("/release_user_from_shift")
    public void releaseUserFromShift(Verifier verifier,
                                     @RequestParam("shift_id") int shift_id,
                                     @RequestParam("accept") boolean accept,
                                     HttpServletRequest httpServletRequest) {
        verifier.ensure(hasRole(Constants.ADMIN));
        accessor.with(access -> {
            String actionUrl = httpServletRequest.getRequestURI() + "?shift_id=" + shift_id;
            Notification notification = access.notification.fromActionURL(actionUrl);
            if (notification == null) {
                System.out.println("Fant ikke notifikasjon med den denne URL-en i databasen:\n" + actionUrl);
                return;
            } else if (notification.getClosed()) {
                System.out.println("Denne notifikasjonen har allerede blitt behandlet!");
                return;
            }
            Shift shift = access.shift.getShiftFromId(shift_id);
            if (accept) {
                User user = shift.getUser();
                access.shift.transferOwnership(shift, null);
                String message = "Du har blitt frigjort fra følgende vakt:\n " +
                        shift.getStartTime() + " til " + shift.getEndTime();
                access.notification.generateMessageNotification(user, message);
            } else {
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
            access.shift.unlockShift(shift);
        });
    }
}

