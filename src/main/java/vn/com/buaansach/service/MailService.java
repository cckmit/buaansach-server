package vn.com.buaansach.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import vn.com.buaansach.model.entity.UserEntity;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class MailService {
    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";
    private final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;
    @Value("${app.mail.from}")
    private String host;
    @Value("${app.mail.enable}")
    private String enableSendMail;
    @Value("${app.mail.client-base-url}")
    private String clientBaseUrl;

    public MailService(JavaMailSender javaMailSender,
                       MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String senderName, String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);
        if (!Boolean.parseBoolean(enableSendMail)) return;
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(host, senderName);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(UserEntity userEntity, String templateName, String titleKey) {
        if (!Boolean.parseBoolean(enableSendMail)) return;
        if (userEntity.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", userEntity.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(userEntity.getLangKey() == null ? "" : userEntity.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, userEntity);
        context.setVariable(BASE_URL, clientBaseUrl);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        String senderName = messageSource.getMessage("email.sender.name", null, locale);
        sendEmail(senderName, userEntity.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(UserEntity userEntity) {
        if (!Boolean.parseBoolean(enableSendMail)) return;
        log.debug("Sending activation email to '{}'", userEntity.getEmail());
        sendEmailFromTemplate(userEntity, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(UserEntity userEntity) {
        if (!Boolean.parseBoolean(enableSendMail)) return;
        log.debug("Sending creation email to '{}'", userEntity.getEmail());
        sendEmailFromTemplate(userEntity, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(UserEntity userEntity) {
        if (!Boolean.parseBoolean(enableSendMail)) return;
        log.debug("Sending password reset email to '{}'", userEntity.getEmail());
        sendEmailFromTemplate(userEntity, "mail/passwordResetEmail", "email.reset.title");
    }
}
