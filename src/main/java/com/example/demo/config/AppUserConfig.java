package com.example.demo.config;

import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppUserConfig {

    @Bean
    public AppUserService appUserService(AppUserRepository repository) {
        return new AppUserService(repository);
    }
}
