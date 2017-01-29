package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Nicole on 16.01.2017.
 */
@Controller
public class WelcomeController extends NavBarController {

    @Authorize("/login")
    @GetMapping("/")
    public String showSlash(Model model) {
        model.addAttribute("activeSchedulePage", "active");
        return "site/user/schedule";
    }
}
