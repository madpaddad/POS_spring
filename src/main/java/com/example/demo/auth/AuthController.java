package com.example.demo.auth;

import com.example.demo.dto.auth.LoginDto;
import com.example.demo.dto.auth.TokenDto;
import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    //    @Autowired
    private AuthService authService;

//    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

//    @PreAuthorize("isAnonymous()")
//    @PostMapping()
//    @ResponseBody
//    public UserDetails Login(String phoneNumber){
//        return this.authService.loadUserByUsername(phoneNumber);
//    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginDto loginDto) {

        log.info("your phone number: ",loginDto.getPhoneNumber());
        return authService.login(loginDto);
    }


}
