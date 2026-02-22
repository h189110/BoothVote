package com.birkt.boothvote.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public EmailService(Optional<JavaMailSender> mailSender) {
        this.mailSender = mailSender.orElse(null);
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        if (mailSender == null) {
            System.err.println("EmailService: mailSender is not configured. Cannot send email to " + to);
            return;
        }

        if (fromEmail == null || fromEmail.isEmpty()) {
            System.err.println("EmailService: 'spring.mail.username' is not configured. Using 'no-reply@boothvote.com' as sender.");
            fromEmail = "no-reply@boothvote.com";
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailException e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
