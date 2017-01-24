package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        accessor.with(access -> {
            model.addAttribute("competences", access.competence.getCompetencesNames());
        });

        return "admin/createuser";
    }

    @Authorize("/")
    @GetMapping("/create/competence")
    public String competence(Model model) {
        return "admin/competence";
    }

    @Authorize("/")
    @GetMapping("/message")
    public String showMessaging(Model model) {
        accessor.with(accessContext -> {
            model.addAttribute("users", accessContext.user.getUsernames());
            model.addAttribute("competences", accessContext.competence.getCompetencesNames());
        });

        return "admin/message";
    }

    @Authorize("/")
    @GetMapping("/createshift")
    public String createShift(Model model) {
        accessor.with(access -> {
            model.addAttribute("competences", access.competence.getCompetencesNames());
            model.addAttribute("users", access.user.getUsernames());
        });
        return "admin/createshift";
    }
}
