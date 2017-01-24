package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.*;

/**
 * Created by gards on 12-Jan-17.
 *
 */

@RestController
//@RequestMapping(value = "/api/notifications")
//FIXME: ordne mappings når testing er ferdig ~Gard
public class NotificationController {
    @Autowired
    private AccessContextFactory accessor;

    @Authorize
    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public Model notification(Model model, Verifier verifier) {
        List<Notification> userNotifications = accessor.with(access -> {
            return access.notification.fromUsername(verifier.claims.getSubject());
        });
        List<Notification> adminNotifications = accessor.with(access -> {
            return access.notification.fromCompetence(access.competence.getFromName("Admin"));
        });

        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("adminNotifications", adminNotifications);
        return model;
    }

    @Authorize
    @PostMapping("/api/notifications/close_notification")
    public void closeNotification(Verifier verifier, @RequestParam("notification_id") int notificationId){
        accessor.with(access -> {
            Notification notification = access.notification.fromId(notificationId);

            verifier.ensure(or(isUser(notification.getUser().getUsername()), hasRole(Constants.ADMIN)));

            access.notification.closeNotification(notification);
        });
    }

    @Authorize
    @PostMapping("/api/notifications/generate_transfer_request_notification")
    public void generateTransferRequestNotification(Verifier verifier,
                                                    @RequestParam("shift_id") int shift_id,
                                                    @RequestParam("user_id") int user_id){
        accessor.with(access -> {
            User shiftOwner = access.user.fromUsername(verifier.claims.getSubject());
            User requestedOwner = access.user.fromID(user_id);
            Shift shift = access.shift.getShiftFromId(shift_id);

            verifier.ensure(or(isUser(shift.getUser().getUsername()), hasRole(Constants.ADMIN)));

            String message = shiftOwner.getFirstName() + " " + shiftOwner.getLastName() + " forespør om du kan ta følgende vakt:\n" +
                    shift.getStartTime() + " til " + shift.getEndTime();

            String actionUrl = "/api/shift/pass_notification_to_admin?shift_id="+shift_id+"&user_id="+user_id;

            access.notification.generateTransferRequestNotification(message, actionUrl, requestedOwner);
        });
    }
}
