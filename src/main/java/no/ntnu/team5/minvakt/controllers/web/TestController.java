package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Knut on 17.01.2017.
 */
@Controller
public class TestController extends NavBarController {
    @Autowired
    AccessContextFactory accessor;

    @Authorize("")
    @GetMapping("/test")
    public String test() {
        return "YourSchedule";
    }
}
