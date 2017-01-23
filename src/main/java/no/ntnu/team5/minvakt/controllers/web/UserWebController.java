package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
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

import static no.ntnu.team5.minvakt.security.auth.verify.Verifier.isUser;

/**
 * Created by alan on 13/01/2017.
 */

@Controller
@RequestMapping("/user")
public class UserWebController extends NavBarController {
    @Autowired
    private AccessContextFactory accessor;

    @Authorize("/")
    @RequestMapping("/{username}")
    public String show(Verifier verifier, @PathVariable("username") String username, Model model) {
        verifier.ensure(Verifier.or(Verifier.isUser(username), Verifier.isUser(Constants.ADMIN)));

        model.addAttribute("user", accessor.with(access -> {
            return access.user.toModel(access.user.fromUsername(username));
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

        model.addAttribute("shifts", shifts);
        return "nextshifts";
    }

    @Authorize
    @RequestMapping("/{username}/availability")
    public String registerAvailability(@PathVariable("username") String username,
                               Model model,
                               Verifier verify) {
        verify.ensure(isUser(username));
        model.addAttribute("makeAvailableModel", new MakeAvailableModel());
        return "AddAvailability";
    }

    @Authorize("/")
    @RequestMapping("/{username}/absence")
    public String absence(@PathVariable ("username") String username,
                          Model model,
                          Verifier verify) {
        verify.ensure(isUser(username));

        return "absence";
    }

    @Authorize("/")
    @GetMapping("/profile")
    public String getProfile(Verifier verifier, Model model) {
        String username = verifier.claims.getSubject();

        accessor.with(accessContext -> {
            User user = accessContext.user.fromUsername(username);
            model.addAttribute("user", user);
        });

        model.addAttribute("passwordResetWithAuth", new PasswordResetWithAuth());

        return "useraccount";
    }

    @Authorize("/")
    @GetMapping("/list")
    public String users(Model model) {
        model.addAttribute("users", accessor.with(accessContext -> {
            return accessContext.user.toModel(accessContext.user.getAllContactInfo());
        }));

        return "employeeoverview";
    }
}
