package com.example.demo.entity.app_user;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AppUserRequest {
    @NotBlank
    private String emailAddress;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotNull
    private List<UserAuthority> authorityList;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserAuthority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<UserAuthority> authorityList) {
        this.authorityList = authorityList;
    }
}
