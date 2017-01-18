package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.model.NotificationModel;
import no.ntnu.team5.minvakt.model.UserModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by alan on 17/01/2017.
 */

@Controller
@RequestMapping("/shift")
public class ShiftWebController {
    @Autowired
    private AccessContextFactory accessor;

    @Authorize
    @GetMapping
    public String site(Model model, Verifier verifier) {
        accessor.with(access -> {
            model.addAttribute("shifts", ShiftAccess.toModel(access.shift.getAllCurrentMonth()));

            String username = verifier.claims.getSubject();
            List<Notification> notifications = access.notification.fromUsername(username);
            List<NotificationModel> notificationModels = access.notification.convertToModel(notifications);
            model.addAttribute("notifications", notificationModels);

            /*UserModel userModel = UserAccess.toModel(access.user.fromUsername(username));
            model.addAttribute("user", userModel);*/

            model.addAttribute("navbar", access.user.getNavbar(username, notificationModels));
        });


        return "schedule";
    }
}
