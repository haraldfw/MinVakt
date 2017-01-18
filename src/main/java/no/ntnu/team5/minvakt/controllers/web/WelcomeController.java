package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
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
        List<ShiftModel> shifts = accessor.with(access -> {
            return ShiftAccess.toModel(access.shift.getUsersShiftNextDays(verifier.claims.getSubject(), 7));
        });

        List<ShiftModel> upcoming = shifts.subList(1, shifts.size());

        model.addAttribute("dates", DATES);
        model.addAttribute("first", shifts.get(0));
        model.addAttribute("upcoming", upcoming);

        return "welcome";
    }
}
