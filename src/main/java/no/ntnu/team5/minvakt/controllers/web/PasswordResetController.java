package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.User;
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
@RequestMapping("/passwordreset")
public class PasswordResetController {

    @Autowired
    private UserAccess userAccess;

    @GetMapping("/{username}/{secretKey}")
    public String show(Model model,
                       @PathVariable("username") String username,
                       @PathVariable("secretKey") String secretKey) {
        User user = userAccess.getUserFromSecretKey(username, secretKey);
        if(user == null) {
            model.addAttribute("keyInvalid", true);
        } else {
            PasswordResetInfo pwrInfo = new PasswordResetInfo();
            pwrInfo.setUsername(username);
            pwrInfo.setSecretKey(secretKey);
            model.addAttribute("passwordResetInfo", pwrInfo);
            model.addAttribute("username", user.getUsername());
        }
        return "password_reset";
    }
}
