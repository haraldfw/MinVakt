package no.ntnu.team5.minvakt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by Harald on 14.01.2017.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email to the specified recipient with the specified info
     *
     * @param to      Address in 'to'-field of the email
     * @param subject Subject of the email
     * @param text    Body of the email
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage original = new SimpleMailMessage();
        original.setTo(to);
        original.setSubject(subject);
        original.setText(text);
        sendEmailMessage(original);
    }

    /**
     * Sends a {@link SimpleMailMessage} object. The message object has to be populated before sending.
     *
     * @param msg The message-object to be sent
     */
    @Async
    public void sendEmailMessage(SimpleMailMessage msg) {
        try {
            mailSender.send(msg);
        } catch (MailException e) {
            logger.warn("Error sending email to '" + Arrays.toString(msg.getTo()) + "' with subject '" + msg.getSubject() + "'");
        }
    }
}
