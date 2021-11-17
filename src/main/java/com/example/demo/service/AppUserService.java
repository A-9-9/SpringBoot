package com.example.demo.service;


import com.example.demo.converter.AppUserConverter;
import com.example.demo.entity.app_user.AppUser;
import com.example.demo.entity.app_user.AppUserRequest;
import com.example.demo.entity.app_user.AppUserResponse;
import com.example.demo.entity.app_user.UserAuthority;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnprocessableEntityException;
import com.example.demo.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

public class AppUserService {

    private AppUserRepository repository;
    private BCryptPasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AppUserResponse createUser(AppUserRequest request) {
        Optional<AppUser> appUserOp = repository.findByEmailAddress(request.getEmailAddress());
        if (appUserOp.isPresent()) {
            throw new UnprocessableEntityException("This email is exists.");
        }

        AppUser appUser = AppUserConverter.toAppUser(request);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        repository.insert(appUser);
        return AppUserConverter.toAppUserResponse(appUser);

    }

    public AppUserResponse getUserResponseById(String id) {
        AppUser appUser
                = repository.findById(id).orElseThrow(() -> new NotFoundException("Can't find user."));
        AppUserResponse appUserResponse = AppUserConverter.toAppUserResponse(appUser);

        return appUserResponse;
    }

    public AppUser getUserByEmail(String email) {
        return repository.findByEmailAddress(email).orElseThrow(() -> new NotFoundException("Can't find user."));
    }

    public List<AppUserResponse> getUserResponses() {
        List<AppUser> appUserList = repository.findAll();
        return AppUserConverter.toAppUserResponses(appUserList);
    }

}
