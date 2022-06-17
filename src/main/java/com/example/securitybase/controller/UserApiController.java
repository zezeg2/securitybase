package com.example.securitybase.controller;

import com.example.securitybase.config.auth.PrincipalDetails;
import com.example.securitybase.repository.UserRepository;
import com.example.securitybase.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public abstract class UserApiController {
    protected final UserRepository userRepository;
    protected final UserService userService;

    public Long getSessionId(Authentication authentication){
        return ((PrincipalDetails) authentication.getPrincipal()).getUser().getId();
    }
}
