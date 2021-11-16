package com.example.demo.service;

import com.example.demo.entity.app_user.AppUser;
import com.example.demo.entity.app_user.SpringUser;
import com.example.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpringUserService implements UserDetailsService {

    @Autowired
    AppUserService appUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AppUser appUser = appUserService.getUserByEmail(username);
            
            return new SpringUser(appUser);
//            List<SimpleGrantedAuthority> authorities = appUser.getAuthorityList()
//                    .stream().map(a -> new SimpleGrantedAuthority(a.name()))
//                    .collect(Collectors.toList());
//            return new User(appUser.getEmailAddress(), appUser.getPassword(), authorities);
        } catch (NotFoundException e){
            throw new UsernameNotFoundException("Username is wrong.");
        }
    }
}
