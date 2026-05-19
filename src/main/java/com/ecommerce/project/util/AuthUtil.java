package com.ecommerce.project.util;

import com.ecommerce.project.model.UserInfo;
import com.ecommerce.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo =
                userRepository.findByUserName(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userInfo.getEmail();
    }

    public UserInfo loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo =
                userRepository.findByUserName(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userInfo;
    }
}