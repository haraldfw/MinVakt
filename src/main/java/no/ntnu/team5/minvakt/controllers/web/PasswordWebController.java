package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.PasswordResetInfo;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Harald on 15.01.2017.
 */
@Controller
@RequestMapping("/password")
public class PasswordWebController extends NavBarController {

    @Autowired
    private AccessContextFactory accessor;

    @GetMapping("/reset")
    public String show(Model model,
                       @RequestParam("username") String username,
                       @RequestParam("resetkey") String resetKey) {

        accessor.with(access -> {

            User user = access.user.getUserFromSecretKey(username, resetKey);
            if (user == null) {
                model.addAttribute("keyInvalid", true);
            } else {
                PasswordResetInfo pwrInfo = new PasswordResetInfo();
                pwrInfo.setUsername(username);
                pwrInfo.setResetKey(resetKey);
                model.addAttribute("passwordResetInfo", pwrInfo);
                model.addAttribute("username", user.getUsername());
            }
        });

        return "site/password/reset";
    }

    @GetMapping("/forgot")
    public String show(Model model) {
        return "site/password/forgot";
    }

    @Authorize("/")
    @GetMapping("/change")
    public String changePw() {
        return "password/change";
    }
}
