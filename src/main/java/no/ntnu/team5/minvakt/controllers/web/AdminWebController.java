package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.model.NewCompetence;
import no.ntnu.team5.minvakt.model.NewShift;
import no.ntnu.team5.minvakt.model.NewUser;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Harald Floor Wilhelmsen on 13.01.2017.
 */
@Controller
@RequestMapping("/admin")
public class AdminWebController extends NavBarController {
    @Autowired
    private AccessContextFactory accessor;

    @Authorize("/")
    @RequestMapping(method = RequestMethod.GET)
    public String show(Model model) {
        model.addAttribute("newUser", new NewUser());
        // TODO use the competence object from the db package instead of passing names

        model.addAttribute("competences", accessor.with(access -> {
            return access.competence.getCompetencesNames();
        }));
        model.addAttribute("newShift", new NewShift());
        model.addAttribute("competences", accessor.with(access -> {
            return access.competence.getCompetencesNames();
        }));
        model.addAttribute("newCompetence", new NewCompetence());

        return "/message";
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
}
