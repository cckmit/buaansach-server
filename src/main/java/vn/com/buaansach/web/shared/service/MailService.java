package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
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
import vn.com.buaansach.entity.enumeration.UserType;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.shared.repository.user.UserProfileRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailService {
    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";
    private final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;
    private final UserProfileRepository userProfileRepository;
    @Value("${app.mail.from}")
    private String host;
    @Value("${app.mail.enable}")
    private String enableSendMail;
    @Value("${app.mail.cms-ui-url}")
    private String cmsBaseUrl;
    @Value("${app.mail.customer-ui-url}")
    private String customerBaseUrl;

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
    public void sendEmailFromTemplate(UserEntity userEntity, String domainType, String templateName, String titleKey) {
        if (!Boolean.parseBoolean(enableSendMail)) return;
        String baseUrl = domainType.equals(Constants.CUSTOMER_UI_DOMAIN) ? customerBaseUrl : cmsBaseUrl;
        if (userEntity.getUserEmail() == null) {
            log.debug("User {} doesn't have and email", userEntity.getUserLogin());
            return;
        }
        UserProfileEntity profileEntity = userProfileRepository.findOneByUserGuid(userEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_PROFILE_NOT_FOUND));
        Locale locale = Locale.forLanguageTag(profileEntity.getLangKey() == null ? Constants.DEFAULT_LANGUAGE : profileEntity.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, userEntity);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        String senderName = messageSource.getMessage("email.sender.name", null, locale);
        sendEmail(senderName, userEntity.getUserEmail(), subject, content, false, true);
    }

//    @Async
//    public void sendActivationEmail(UserEntity userEntity) {
//        if (!Boolean.parseBoolean(enableSendMail)) return;
//        log.debug("Sending activation email to '{}'", userEntity.getUserEmail());
//        sendEmailFromTemplate(userEntity, Constants.CMS_UI_DOMAIN, "mail/activationEmail", "email.activation.title");
//    }
//
//    @Async
//    public void sendCreationEmail(UserEntity userEntity) {
//        if (!Boolean.parseBoolean(enableSendMail)) return;
//        log.debug("Sending creation email to '{}'", userEntity.getUserEmail());
//        sendEmailFromTemplate(userEntity, Constants.CMS_UI_DOMAIN, "mail/creationEmail", "email.activation.title");
//    }

    @Async
    public void sendPasswordResetMail(UserEntity userEntity, String domainType) {
        if (!Boolean.parseBoolean(enableSendMail)) return;
        log.debug("Sending password reset email to '{}'", userEntity.getUserEmail());
        if (domainType.equals(Constants.CUSTOMER_UI_DOMAIN)) {
            sendEmailFromTemplate(userEntity, domainType, "mail/customerPasswordResetEmail", "email.reset.title");
        } else {
            sendEmailFromTemplate(userEntity, domainType, "mail/passwordResetEmail", "email.reset.title");
        }

    }
}
