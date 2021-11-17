package com.example.demo.entity.app_user;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "AppUser")
public class AppUser {
    private String id;
    private String emailAddress;
    private String password;
    private String name;
    private List<UserAuthority> authorityList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
