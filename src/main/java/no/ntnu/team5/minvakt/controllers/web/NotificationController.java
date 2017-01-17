package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
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
    public Model notification(Model model, Verifier verifier) {
        List<Notification> notifications = accessor.with(access -> {
            return access.notification.fromUsername(verifier.claims.getSubject()); //TODO: Use a model here
        });

        model.addAttribute("notifications", notifications);
        return model;
    }
}
