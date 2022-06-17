package com.example.securitybase.controller;

import com.example.securitybase.domain.user.User;
import com.example.securitybase.domain.user.UserDto;
import com.example.securitybase.repository.UserRepository;
import com.example.securitybase.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerApiController extends UserApiController {
    public ManagerApiController(UserRepository userRepository, UserService userService) {
        super(userRepository, userService);
    }

    @GetMapping
    public User manager(Authentication authentication){
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping
    public void updateManager(Authentication authentication, UserDto userDto){
        userService.update(getSessionId(authentication), userDto);
    }
}
