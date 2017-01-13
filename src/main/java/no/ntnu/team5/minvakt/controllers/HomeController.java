package no.ntnu.team5.minvakt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

/**
 * Created by gards on 10-Jan-17.
 *
 */

@Controller
public class HomeController {
    private ArrayList<String> texts = new ArrayList<String>();

    public HomeController() {
        texts.add("Hei");
        texts.add("På");
        texts.add("Deg");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model){
        model.addAttribute("texts", texts).addAttribute("lol","lol");
        return "home";
    }
}