package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Notification;
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
}
