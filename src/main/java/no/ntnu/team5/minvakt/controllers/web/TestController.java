package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Competence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Knut on 17.01.2017.
 */
@Controller
public class TestController {
    @Autowired
    AccessContextFactory accessor;

    @GetMapping("/test")
    public String test() {
        return "YourSchedule";
    }
}
