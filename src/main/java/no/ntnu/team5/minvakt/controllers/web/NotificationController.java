package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by gards on 12-Jan-17.
 *
 */

@Controller
@RequestMapping(value = "/notifications")
public class NotificationController {
    @Autowired
    private AccessContextFactory accessor;

    @Authorize
    @RequestMapping(method = RequestMethod.GET)
    public String notification(Model model, Verifier verifier) {
        List<Notification> userNotifications = accessor.with(access -> {
            return access.notification.fromUsername(verifier.claims.getSubject());
        });
        List<Notification> adminNotifications = accessor.with(access -> {
            return access.notification.fromCompetence(access.competence.getFromName("Admin"));
        });

        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("adminNotifications", adminNotifications);
        return "notifications";
    }
}
