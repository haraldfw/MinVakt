package no.ntnu.team5.minvakt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gards on 10-Jan-17.
 *
 */

@Controller
public class Fragment_testController {
    @RequestMapping(value = "/fragment", method = RequestMethod.GET)
    public String home(Model model){
        return "fragment_test";
    }
}