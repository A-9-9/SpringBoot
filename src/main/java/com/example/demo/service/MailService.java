package com.example.demo.service;

import com.example.demo.config.MailConfig;
import com.example.demo.entity.Product;
import com.example.demo.entity.SendMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;


public class MailService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String LOG_MAIL;
    private final long tag;
    private final List<String> mailMessage;

    private JavaMailSenderImpl mailSender;

    public MailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
        this.LOG_MAIL = mailSender.getUsername();
        this.tag = System.currentTimeMillis();
        this.mailMessage = new ArrayList<>();
    }

    public void sendMail(SendMailRequest request) {
        sendMail(request.getSubject(), request.getContent(), Arrays.asList(request.getReceivers()));
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
            printMessages();
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public void sendDeleteProductMail(String id) {
        String content = String.format("There's a product deleted (%s).", id);
        sendMail("Product Delete.", content, Collections.singletonList(LOG_MAIL));
    }

    public void sendCreateProductMail(String id) {
        String content = String.format("There's a new product (%s).", id);
        sendMail("New Product.", content, Collections.singletonList(LOG_MAIL));
    }

    private void printMessages() {
        System.out.println("-----------------");
        mailMessage.forEach(System.out::println);
    };

    @PreDestroy
    public void preDestroy() {
        System.out.println("##################");
        System.out.printf("The mail service is destroy now! %d.\n\n", tag);
    };

}
