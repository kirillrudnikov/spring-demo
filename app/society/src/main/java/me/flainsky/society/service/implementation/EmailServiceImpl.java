package me.flainsky.society.service.implementation;

import lombok.AllArgsConstructor;
import me.flainsky.society.dto.SignUpForm;
import me.flainsky.society.dto.UserDto;
import me.flainsky.society.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public String getHtmlContent(SignUpForm form, String link) {
        Context context = new Context();
        context.setVariable("email", form.getEmail());
        context.setVariable("link", link);

        return templateEngine.process("email", context);
    }

    @Async
    public void sendSimpleMail(String to, String from, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);

        logger.info("Mail sent to: {}, with subject: {}", to, subject);
    }

    @Async
    public void sendComplexMail(String to, String from, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(message);

            logger.info("Mail sent to: {}, with subject: {}", to, subject);
        } catch (MessagingException exception) {
            logger.error(exception.getMessage());
        }
    }

}