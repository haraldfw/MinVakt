package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.model.NewUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Harald Floor Wilhelmsen on 13.01.2017.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping(method = RequestMethod.GET)
    public String show(Model model) {
        model.addAttribute("newUser", new NewUser());
        return "admin";
    }
}
