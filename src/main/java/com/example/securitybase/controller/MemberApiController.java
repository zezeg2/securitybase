package com.example.securitybase.controller;

import com.example.securitybase.domain.user.User;
import com.example.securitybase.domain.user.UserDto;
import com.example.securitybase.repository.UserRepository;
import com.example.securitybase.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MemberApiController extends UserApiController {

    public MemberApiController(UserRepository userRepository, UserService userService) {
        super(userRepository, userService);
    }

    @GetMapping("/member")
    public User member(Authentication authentication) {
        return userRepository.findById(getSessionId(authentication)).get();
    }

    @PostMapping("/member")
    public void updateMember(Authentication authentication, UserDto userDto) {
        userService.update(getSessionId(authentication), userDto);
    }

}
