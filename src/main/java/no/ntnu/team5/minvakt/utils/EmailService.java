package no.ntnu.team5.minvakt.utils;

import no.ntnu.team5.minvakt.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Harald on 14.01.2017.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    ContentBuilder contentBuilder;

    /**
     * Sends an email to the specified recipient with the specified info
     *
     * @param to      Address in 'to'-field of the email
     * @param subject Subject of the email
     * @param text    Body of the email
     */
    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage original = new SimpleMailMessage();
        original.setTo(to);
        original.setSubject(subject);
        original.setText(text);
        mailSender.send(original);
    }


    /**
     * Sends message in HTML-format
     *
     * @param to           Recipient
     * @param subject      Subject-line of email
     * @param templateName Name of template to base email on
     * @param values       Values to inject into template
     */
    @Async
    public void sendEmail(String to, String subject, String templateName, Map<String, String> values) {
        String text = contentBuilder.build(templateName, values);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
        };
        mailSender.send(messagePreparator);
    }

    /**
     * Send an email with the user_created template to the given email
     *
     * @param username       Username of created user
     * @param email          Address of created user
     * @param resetKey       ResetKey of created user
     * @param resetKeyExpiry ResetKeyExpiry of created User
     * @throws UnsupportedEncodingException If 'UTF-8' is not a supported encoding
     */
    public void userCreated(String username, String email, String resetKey, Date resetKeyExpiry) throws UnsupportedEncodingException {
        String encodedKey = URLEncoder.encode(resetKey, "UTF-8");
        String subject = "User has been created for you in MinVakt";
        String link = "http://" + Constants.HOSTNAME + "/password/reset?username=" +
                username + "&resetkey=" + encodedKey;
        String expiry = new SimpleDateFormat("yyyy-M-d kk:mm").format(resetKeyExpiry);

        Map<String, String> vars = new HashMap<>();
        vars.put("link", link);
        vars.put("expiry", expiry);
        vars.put("username", username);

        sendEmail(email, subject, "email/user_created", vars);
    }
}
