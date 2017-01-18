package no.ntnu.team5.minvakt.controllers.web;

import io.jsonwebtoken.Jwts;
import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.MakeAvailableModel;
import no.ntnu.team5.minvakt.model.PasswordResetWithAuth;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.hasRole;
import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.isUser;
import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.or;

/**
 * Created by alan on 13/01/2017.
 */

@Controller
@RequestMapping("/user")
public class UserWebController {
    @Autowired
    private AccessContextFactory accessor;

    @Authorize("/error")
    @RequestMapping("/{username}")
    public String show(Verifier verifier, @PathVariable("username") String username, Model model) {
        verifier.ensure(Verifier.isUser(username));

        model.addAttribute("user", accessor.with(access -> {
            return UserAccess.toModel(access.user.fromUsername(username));
        }));

        return "useraccount";
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
        System.out.println(shifts.size());
        model.addAttribute("shifts", shifts);
        return "nextshifts";
    }

    @Authorize
    @RequestMapping("/{username}/registeravailability")
    public String getNextShift(@PathVariable("username") String username,
                               Model model,
                               Verifier verify) {
        verify.ensure(isUser(username));
        model.addAttribute("makeAvailableModel", new MakeAvailableModel());
        return "registeravailability";
    }

    @Authorize
    @GetMapping("/profile")
    public String getProfile(Verifier verifier, Model model) {
        String username = verifier.claims.getSubject();
        verifier.ensure(or(isUser(username), hasRole(Constants.ADMIN)));
        User user = accessor.with(accessContext -> {
            return accessContext.user.fromUsername(username);
        });
        model.addAttribute("passwordResetWithAuth", new PasswordResetWithAuth());

        model.addAttribute("user", user);
        return "useraccount";
    }

//    @ModelAttribute("user")
//    public List<Variety> populateVarieties() {
//        return this.varietyService.findAll();
//    }
}
