package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.model.NotificationModel;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.expression.Dates;

import java.util.List;
import java.util.Locale;

/**
 * Created by Nicole on 16.01.2017.
 */
@Controller
public class WelcomeController {

    @Autowired
    private AccessContextFactory accessor;

    static private final Dates DATES = new Dates(new Locale("nb"));

    @Authorize("/error")
    @RequestMapping("/welcome")
    public String welcome(Verifier verifier, Model model) {

        accessor.with(access -> {
            List<ShiftModel> shifts = ShiftAccess.toModel(access.shift.getUsersShiftNextDays(verifier.claims.getSubject(), 7));

            ShiftModel first = null;
            List<ShiftModel> upcoming = null;
            int shiftsSize = shifts.size();
            if (shiftsSize > 0) {
                first = shifts.get(0);
            }
            if (shiftsSize > 1) {
                upcoming = shifts.subList(1, shiftsSize);
            }

            String username = verifier.claims.getSubject();
            List<Notification> notifications = access.notification.fromUsername(username);
            List<NotificationModel> notificationModels = access.notification.convertToModel(notifications);
            model.addAttribute("notifications", notificationModels);

            model.addAttribute("dates", DATES);
            model.addAttribute("first", first);
            model.addAttribute("upcoming", upcoming);
        });
        return "welcome";
    }
}
