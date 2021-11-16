package com.example.demo.service;

import com.example.demo.entity.mail.SendMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MailService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final JavaMailSenderImpl mailSender;
    private final long tag;
    private final List<String> mailMessage;
    private final String LOG_EMAIL;

    public MailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
        this.tag = System.currentTimeMillis();
        this.mailMessage = new ArrayList<String>();
        this.LOG_EMAIL = mailSender.getUsername();
    }


    public void sendMail(SendMailRequest request) {
        String subject = request.getSubject();
        String content = request.getContent();
        String[] receivers = request.getReceivers();

        sendMail(subject, content, Stream.of(receivers).collect(Collectors.toList()));
    }

    public void sendMail(String subject, String content, List<String> receivers) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailSender.getUsername());
        message.setTo(receivers.stream().toArray(String[]::new));
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            mailMessage.add(content);
            printMessage();
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public void sendNewProductMail(String id) {
        String content = String.format("There's a new Product created (%s)", id);
        sendMail("Product created.", content, Collections.singletonList(LOG_EMAIL));

    }

    public void sendDeleteProductMail(String id) {
        String content = String.format("There's a Product deleted (%s)", id);
        sendMail("Product deleted.", content, Collections.singletonList(LOG_EMAIL));
    }

    private void printMessage() {
        System.out.println("--------------------");
        mailMessage.forEach(System.out::println);
    };

    @PreDestroy
    public void preDestroy() {
        System.out.println("###########");
        System.out.printf("Spring Boot is going to terminated the Mail Service %d.\n\n", tag);
    }

}
