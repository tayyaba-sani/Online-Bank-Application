package com.notification.Notification.service;


import com.notification.Notification.config.NotificationServiceConfig;
import com.notification.Notification.dto.Accounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;

@Component
public class JavaMailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(JavaMailSenderService.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final NotificationServiceConfig notificationServiceConfig;

    public JavaMailSenderService(JavaMailSender mailSender, SpringTemplateEngine templateEngine, NotificationServiceConfig notificationServiceConfig) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.notificationServiceConfig = notificationServiceConfig;
    }

    @RabbitListener(queues = "${queue.rabbitmq.queue}")
    public void consumeMessageFromQueue(Accounts accounts) {
        if (accounts.getOperations().size() == 1) {
            sendEmail(accounts, notificationServiceConfig.getSubjectForCreateAccount(), "email/create-account-email-template");
        } else {
            sendEmail(accounts, notificationServiceConfig.getSubject() + accounts.getAccountNumber(), "email/email-template");
        }
    }

    public void sendEmail(Accounts accounts, String subject, String templateLocation) {
        try {
            String from = "${javamail.sender.from}";
            String to = accounts.getCustomer().getEmail();
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            Context context = new Context();
            context.setVariable("accounts", accounts);
            String emailContent = templateEngine.process(templateLocation, context);
            helper.setText(emailContent, true);
            mailSender.send(mimeMessage);
        } catch (Exception exception) {
            logger.error("JavaMailSenderService: Email Exception Occurred with account Number is : " + accounts.getAccountNumber());
            logger.error("Error is " + exception.getCause().getMessage());
            logger.error(exception.getStackTrace().toString());
        }
    }


}
