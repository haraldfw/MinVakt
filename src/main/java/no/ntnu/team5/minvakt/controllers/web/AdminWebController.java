package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.model.NewUser;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Harald Floor Wilhelmsen on 13.01.2017.
 */
@Controller
@RequestMapping("/admin")
public class AdminWebController {
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

        return "admin";
    }
}
