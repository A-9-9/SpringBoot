package com.example.demo.converter;

import com.example.demo.entity.app_user.AppUser;
import com.example.demo.entity.app_user.AppUserRequest;
import com.example.demo.entity.app_user.AppUserResponse;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserConverter {
    public static AppUser toAppUser(AppUserRequest request) {
        AppUser appUser = new AppUser();
        appUser.setName(request.getName());
        appUser.setAuthorityList(request.getAuthorityList());
        appUser.setEmailAddress(request.getEmailAddress());
        appUser.setPassword(request.getPassword());
        return appUser;
    }

    public static AppUserResponse toAppUserResponse(AppUser appUser) {
        AppUserResponse appUserResponse = new AppUserResponse();
        appUserResponse.setId(appUser.getId());
        appUserResponse.setAuthorityList(appUser.getAuthorityList());
        appUserResponse.setName(appUser.getName());
        appUserResponse.setEmailAddress(appUser.getEmailAddress());
        return appUserResponse;
    }

    public static List<AppUserResponse> toAppUserResponses(List<AppUser> appUserList) {
        return appUserList.stream().map(AppUserConverter::toAppUserResponse).collect(Collectors.toList());
    }
}
