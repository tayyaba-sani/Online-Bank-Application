package com.notification.Notification.config;

import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "notification")
@ToString
public class NotificationServiceConfig {
    private String subject;
    private String subjectForCreateAccount;
    private String from;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectForCreateAccount() {
        return subjectForCreateAccount;
    }

    public void setSubjectForCreateAccount(String subjectForCreateAccount) {
        this.subjectForCreateAccount = subjectForCreateAccount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
