package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.model.LoginInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Harald Floor Wilhelmsen on 13.01.2017.
 */
@Controller
public class LoginWebController {

    @GetMapping("/login")
    public String show(Model model) {
        model.addAttribute("loginInfo", new LoginInfo());
        return "login";
    }

}
