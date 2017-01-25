package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Harald Floor Wilhelmsen on 25.01.2017.
 */
@Controller
public class TimebankWebController extends NavBarController {

    @Autowired
    private AccessContextFactory accessor;

    @Authorize("/")
    @GetMapping("/timebank")
    public String show(Model model, Verifier verifier) {
        model.addAttribute("activeTimebankPage", "active");
        accessor.with(accessContext -> {
            model.addAttribute("shifts", accessContext.shift.toModel(
                    accessContext.shift.getTheseThreeDays(verifier.claims.getSubject())
            ));
        });

        return "site/timebank";
    }
}
