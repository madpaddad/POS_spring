package com.example.demo.auth;

import com.example.demo.dto.auth.LoginDto;
import com.example.demo.dto.auth.TokenDto;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/login")
public class AuthController {

//    @Autowired
    private AuthService authService;

//    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping()
    @ResponseBody
    public UserDetails Login(String phoneNumber){
        return this.authService.loadUserByUsername(phoneNumber);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginDto loginDto) {


        return authService.login(loginDto);
    }

}
