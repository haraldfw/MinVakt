package no.ntnu.team5.minvakt.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Nicole on 16.01.2017.
 */

@Controller
public class CreatePassword {
    @RequestMapping("/createpassword")
    public String createpassword() {
        return "createpassword";
    }
}