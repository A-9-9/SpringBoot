package com.example.demo.auth;

import com.example.demo.entity.app_user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class SpringUser implements UserDetails {
    private AppUser appUser;

    public SpringUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getId() {
        return appUser.getId();
    }

    public String getName() {
        return appUser.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return appUser.getAuthorityList()
                .stream()
                .map(u -> new SimpleGrantedAuthority(u.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
