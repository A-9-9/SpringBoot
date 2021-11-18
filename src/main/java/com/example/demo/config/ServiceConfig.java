package com.example.demo.config;

import com.example.demo.auth.UserIdentity;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.MailService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class ServiceConfig {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ProductService productService(ProductRepository productRepository, MailService mailService, UserIdentity userIdentity) {
        System.out.println("Product Service is Created.");
        return new ProductService(productRepository, mailService, userIdentity);
    }


}
