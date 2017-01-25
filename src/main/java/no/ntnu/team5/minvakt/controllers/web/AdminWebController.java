package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftAssign;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Harald Floor Wilhelmsen on 13.01.2017.
 */
@Controller
@RequestMapping("/admin")
public class AdminWebController extends NavBarController {

    @Autowired
    private AccessContextFactory accessor;

    @Authorize("/")
    @GetMapping("/create/user")
    public String createUser(Model model) {
        model.addAttribute("activeAdminCreateUserPage", "active");

        accessor.with(access -> {
            model.addAttribute("competences", access.competence.getCompetencesNames());
        });

        return "site/admin/create/user";
    }

    @Authorize("/")
    @GetMapping("/create/competence")
    public String competence(Model model) {
        model.addAttribute("activeAdminCreateCompPage", "active");
        return "site/admin/create/competence";
    }

    @Authorize("/")
    @GetMapping("/create/shift")
    public String createShift(Model model) {
        model.addAttribute("activeAdminCreateShiftPage", "active");
        accessor.with(access -> {
            model.addAttribute("competences", access.competence.getCompetencesNames());
        });

        return "site/admin/create/shift";
    }

    @Authorize("/")
    @GetMapping("/message")
    public String showMessaging(Model model) {
        model.addAttribute("activeAdminMessagePage", "active");
        accessor.with(accessContext -> {
            model.addAttribute("users", accessContext.user.getUsernames());
            model.addAttribute("competences", accessContext.competence.getCompetencesNames());
        });

        return "site/admin/message";
    }
    @Authorize("/")
    @GetMapping("/assign/{shiftid}")
    public String assignUser(Model model, @PathVariable("shiftid") int shiftid) {
        accessor.with(access -> {
            Shift shift = access.shift.getShiftFromId(shiftid);
            List<User> users = access.availability.listAvailableUsers(access.shift.getFromDateFromId(shiftid), access.shift.getEndDateFromId(shiftid));
            model.addAttribute("users",
                    users
                            .stream()
                            .filter(user -> shift.getCompetences().stream().allMatch(user.getCompetences()::contains))
                            .map(access.user::toModel)
                            .collect(Collectors.toList()));
        });

        return "site/admin/assign/shift";
    }
}
