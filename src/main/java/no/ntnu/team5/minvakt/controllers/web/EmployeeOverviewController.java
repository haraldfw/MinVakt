package no.ntnu.team5.minvakt.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Nicole on 18.01.2017.
 */

@Controller
public class EmployeeOverviewController {
    @RequestMapping("/employeeoverview")
    public String forgotpassword(){
        return "employeeoverview";
    }
}
