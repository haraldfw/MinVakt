package no.ntnu.team5.minvakt.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Knut on 17.01.2017.
 */
@Controller
public class TestController {
    @GetMapping("/test1234")
    public String test() {
        return "YourSchedule";
    }
}
