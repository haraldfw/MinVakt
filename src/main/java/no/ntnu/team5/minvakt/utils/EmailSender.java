package no.ntnu.team5.minvakt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by Harald on 14.01.2017.
 */
@Component
@Scope("singleton")
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        sendEmail(to, subject, text, new SimpleMailMessage());
    }

    public void sendEmail(String to, String subject, String text, SimpleMailMessage original) {
        original.setTo(to);
        original.setSubject(subject);
        original.setText(text);
        mailSender.send(original);
    }
}
