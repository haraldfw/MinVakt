package no.ntnu.team5.minvakt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Harald on 14.01.2017.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    MailContentBuilder mailContentBuilder;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

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
        String text = mailContentBuilder.build(templateName, values);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
        };
        mailSender.send(messagePreparator);
    }

    public String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
