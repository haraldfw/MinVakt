package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.isUser;

/**
 * Created by alan on 13/01/2017.
 */

@Controller
@RequestMapping("/user")
public class UserWebController {
    @Autowired
    private AccessContextFactory accessor;

    @RequestMapping("/{username}")
    public String show(Verifier verifier, @PathVariable("username") String username) {
        verifier.ensure(Verifier.isUser(username));

        return "profile";
    }

    @Authorize
    @RequestMapping("/{username}/nextshifts")
    public String getNextShift(Verifier verifier,
                                         @PathVariable("username") String username,
                                         Model model) {
        verifier.ensure(isUser(username));

        List<ShiftModel> shifts = accessor.with(access -> {
            return access.shift.getShiftsForAUser(username)
                    .stream()
                    .map(shift -> access.shift.toModel(shift)).collect(Collectors.toList());
        });

        model.addAttribute("shifts", shifts);
        return "nextshifts";
    }

//    @ModelAttribute("user")
//    public List<Variety> populateVarieties() {
//        return this.varietyService.findAll();
//    }
}
