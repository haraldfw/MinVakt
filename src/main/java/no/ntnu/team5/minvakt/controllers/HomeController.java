package no.ntnu.team5.minvakt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gards on 10-Jan-17.
 */

@Controller
public class HomeController {
    private String text = "Testlinje";

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("home");
        model.addAttribute("text", text);
        return "home";
    }
}