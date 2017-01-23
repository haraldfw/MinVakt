package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ChangePassword;
import no.ntnu.team5.minvakt.model.ForgottenPassword;
import no.ntnu.team5.minvakt.model.LoginResponse;
import no.ntnu.team5.minvakt.model.PasswordResetInfo;
import no.ntnu.team5.minvakt.security.PasswordUtil;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import no.ntnu.team5.minvakt.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Harald on 15.01.2017.
 */
@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private AccessContextFactory accessor;

    @Autowired
    private EmailService emailService;

    @PostMapping("/forgot")
    public void forgot(@ModelAttribute ForgottenPassword forgotInfo) {
        String input = forgotInfo.getUsernameEmail().trim();

        accessor.with(accessContext -> {
            User user;

            if (input.contains("@")) {
                // input is an email
                user = accessContext.user.fromEmail(input);
            } else {
                // input is a username
                user = accessContext.user.fromUsername(input);
            }


            if (user != null) {
                String resetKey = PasswordUtil.generateSalt();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                Date resetKeyExpiry = c.getTime();
                user.setResetKey(resetKey);
                user.setResetKeyExpiry(resetKeyExpiry);

                accessContext.user.save(user);

                try {
                    String encodedKey = URLEncoder.encode(resetKey, "UTF-8");
                    String subject = "Your password reset request";
                    String link = "http://localhost:8080/password/reset?username=" +
                            user.getUsername() + "&resetkey=" + encodedKey;
                    String expiry = new SimpleDateFormat("yyyy-M-d kk:mm").format(resetKeyExpiry);

                    Map<String, String> vars = new HashMap<>();
                    vars.put("link", link);
                    vars.put("expiry", expiry);

                    emailService.sendEmail(
                            user.getEmail(), subject, "email/forgot_password", vars);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @PostMapping("/reset")
    public void resetPassword(HttpServletResponse response, @ModelAttribute PasswordResetInfo pwrInfo) {
        accessor.with(accessContext -> {
            User user = accessContext.user.getUserFromSecretKey(
                    pwrInfo.getUsername(), pwrInfo.getResetKey());

            if (pwrInfo.getPassword().equals(pwrInfo.getPasswordRepeat()) && user != null) {
                boolean changed = PasswordUtil.setPassword(user, pwrInfo.getPassword());

                if (changed) {
                    user.setResetKey("");
                    user.setResetKeyExpiry(new Date());

                    accessContext.user.save(user);

                    return;
                }
            }

            // If no early exit then the password reset failed
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        });
    }

    @Authorize
    @PostMapping("/change")
    public void change_password(HttpServletResponse response, Verifier verifier, @RequestBody ChangePassword pwInfo) {
        accessor.with(access -> {
            User user = access.user.fromUsername(verifier.claims.getSubject());

            LoginResponse lr = PasswordUtil.login(user, pwInfo.getOldPassword(), false);
            if (!lr.getSuccess()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            boolean changed = PasswordUtil.setPassword(user, pwInfo.getNewPassword());

            if (changed) {
                access.user.save(user);
            } else {
                response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            }
        });
    }
}
