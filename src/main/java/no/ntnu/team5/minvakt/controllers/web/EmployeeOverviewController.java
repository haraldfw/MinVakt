package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Nicole on 18.01.2017.
 */

@Controller
public class EmployeeOverviewController {
    @Authorize("/")
    @RequestMapping("/employeeoverview")
    public String employeeoverview(){
        return "employeeoverview";
    }
}
