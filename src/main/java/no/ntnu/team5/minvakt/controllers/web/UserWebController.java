package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("user", accessor.with(access -> {
            return access.user.toModel(access.user.fromUsername(username));
        }));

        //TODO: display diffrent info depending on role (owner, admin, user)
        return "site/user/profile";
    }

    @Authorize("/")
    @GetMapping("/profile")
    public String getProfile(Verifier verifier, Model model) {
        String username = verifier.claims.getSubject();

        accessor.with(accessContext -> {
            User user = accessContext.user.fromUsername(username);
            model.addAttribute("user", user);
        });

        return "site/user/profile";
    }

    @Authorize("/")
    @GetMapping("/list")
    public String users(Model model) {
        model.addAttribute("users", accessor.with(accessContext -> {
            return accessContext.user.toModel(accessContext.user.getAllContactInfo());
        }));

        return "site/user/list";
    }

    @Authorize("/")
    @GetMapping("/user/schedule")
    public String test() {
        return "site/user/schedule";
    }
}
