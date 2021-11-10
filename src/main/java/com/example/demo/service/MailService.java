package com.example.demo.service;

import com.example.demo.config.MailConfig;
import com.example.demo.entity.SendMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;


public class MailService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private JavaMailSenderImpl mailSender;

    public MailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }
//    @PostConstruct
//    private void init() {
//        mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(mailConfig.getHost());
//        mailSender.setPort(mailConfig.getPort());
//        mailSender.setUsername(mailConfig.getUsername());
//        mailSender.setPassword(mailConfig.getPassword());
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.smtp.auth", mailConfig.isAuthEnable());
//        props.put("mail.smtp.starttls.enable", mailConfig.isStarttlsEnable());
//        props.put("mail.transport.protocol", mailConfig.getProtocol());
//    }

    public void sendMail(SendMailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailSender.getUsername());
        message.setTo(request.getReceivers());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }


    }
}
