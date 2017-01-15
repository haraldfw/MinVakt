package no.ntnu.team5.minvakt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by Harald on 14.01.2017.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendEmail(String to, String subject, String text) {
        sendEmail(to, subject, text, new SimpleMailMessage());
    }

    @Async
    public void sendEmail(String to, String subject, String text, SimpleMailMessage original) {
        original.setTo(to);
        original.setSubject(subject);
        original.setText(text);
        try {
            mailSender.send(original);
        } catch (MailException e) {
            logger.warn("Error sending email to recipient '" + to + "' with subject '" + subject + "'");
        }

    }
}
