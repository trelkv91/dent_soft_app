package com.soft.dental.spring.service;

import com.soft.dental.spring.models.User;
import com.soft.dental.spring.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;

    public SecurityServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userService.findByUsername(userDetails.getUsername());
    }
}
