package no.ntnu.team5.minvakt.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Ingunn on 17.01.2017.
 */
@Controller
public class TestingController {

    @GetMapping("/shiftss")
    public String test() {
        return "AllShifts";
    }
}
