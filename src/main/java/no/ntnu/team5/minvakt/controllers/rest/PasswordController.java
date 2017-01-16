package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ForgottenPassword;
import no.ntnu.team5.minvakt.model.PasswordResetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Harald on 15.01.2017.
 */
@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private AccessContextFactory accessContextFactory;

    @PostMapping("/forgot")
    public void forgot(@ModelAttribute ForgottenPassword forgotInfo) {
        String input = forgotInfo.getUsernameEmail().trim();

        accessContextFactory.with(accessContext -> {
            User user;

            if (input.contains("@")) {
                // input is an email
                user = accessContext.user.fromEmail(input);
            } else {
                // input is a username
                user = accessContext.user.fromUsername(input);
            }


            if (user != null) {

            }
        });
    }

    @PostMapping("/reset")
    public void resetPassword(@ModelAttribute PasswordResetInfo pwrInfo) {

    }
}
