package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by alan on 17/01/2017.
 */

@Controller
@RequestMapping("/shifts")
public class ShiftWebController extends NavBarController {

    @Authorize("/")
    @GetMapping("")
    public String test(Model model) {
        model.addAttribute("activeShiftPage", "active");
        return "site/shifts";
    }
}
