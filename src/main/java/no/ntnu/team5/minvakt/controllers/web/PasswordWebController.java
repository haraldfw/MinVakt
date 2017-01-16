package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ForgottenPassword;
import no.ntnu.team5.minvakt.model.PasswordResetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Harald on 15.01.2017.
 */
@Controller
@RequestMapping("/password")
public class PasswordWebController {

    @Autowired
    private AccessContextFactory accessContextFactory;

    @GetMapping("/reset/{username}/{secretKey}")
    public String show(Model model,
                       @PathVariable("username") String username,
                       @PathVariable("secretKey") String secretKey) {
        accessContextFactory.with(accessContext -> {
            User user = accessContext.user.getUserFromSecretKey(username, secretKey);
            if(user == null) {
                model.addAttribute("keyInvalid", true);
            } else {
                PasswordResetInfo pwrInfo = new PasswordResetInfo();
                pwrInfo.setUsername(username);
                pwrInfo.setSecretKey(secretKey);
                model.addAttribute("passwordResetInfo", pwrInfo);
                model.addAttribute("username", user.getUsername());
            }
        });
        return "password_reset";
    }

    @GetMapping("/forgot")
    public String show(Model model) {
        model.addAttribute("forgotInfo", new ForgottenPassword());
        return "forgotten_password";
    }
}
