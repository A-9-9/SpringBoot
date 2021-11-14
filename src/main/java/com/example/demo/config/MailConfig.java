package com.example.demo.config;

import com.example.demo.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    public static final String GMAIL_SERVICE = "gmailService";
    public static final String YAHOO_MAIL_SERVICE = "yahooMailService";
    @Value("${mail.gmail.host}")
    private String gmailHost;

    @Value("${mail.gmail.port:25}")
    private int gmailPort;

    @Value("${mail.gmail.username}")
    private String gmailUsername;

    @Value("${mail.gmail.password}")
    private String gmailPassword;

    @Value("${mail.yahoo.host}")
    private String yahooHost;

    @Value("${mail.yahoo.port:25}")
    private int yahooPort;

    @Value("${mail.yahoo.username}")
    private String yahooUsername;

    @Value("${mail.yahoo.password}")
    private String yahooPassword;


    @Value("${mail.auth.enable}")
    private boolean authEnable;

    @Value("${mail.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.platform}")
    private String platform;

    @Bean
    @RequestScope
    public MailService mailService() {
        JavaMailSenderImpl mailSender = platform.equalsIgnoreCase("gmail")
                ? getGmailSender()
                : getYahooMailSender();
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", this.isAuthEnable());
        props.put("mail.smtp.starttls.enable", this.isStarttlsEnable());
        props.put("mail.transport.protocol", this.getProtocol());

        System.out.println("Mail Service is Created.");
        return new MailService(mailSender);
    }

    public JavaMailSenderImpl getGmailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.getGmailHost());
        mailSender.setPort(this.getGmailPort());
        mailSender.setUsername(this.getGmailUsername());
        mailSender.setPassword(this.getGmailPassword());
        return mailSender;
    }

    public JavaMailSenderImpl getYahooMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.getYahooHost());
        mailSender.setPort(this.getYahooPort());
        mailSender.setUsername(this.getYahooUsername());
        mailSender.setPassword(this.getYahooPassword());
        return mailSender;
    }


    public String getGmailHost() {
        return gmailHost;
    }

    public int getGmailPort() {
        return gmailPort;
    }

    public String getGmailUsername() {
        return gmailUsername;
    }

    public String getGmailPassword() {
        return gmailPassword;
    }

    public String getYahooHost() {
        return yahooHost;
    }

    public int getYahooPort() {
        return yahooPort;
    }

    public String getYahooUsername() {
        return yahooUsername;
    }

    public String getYahooPassword() {
        return yahooPassword;
    }

    public boolean isAuthEnable() {
        return authEnable;
    }

    public boolean isStarttlsEnable() {
        return starttlsEnable;
    }

    public String getProtocol() {
        return protocol;
    }
}
