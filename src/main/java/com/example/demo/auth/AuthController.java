package com.example.demo.auth;

import com.example.demo.dto.auth.LoginDto;
import com.example.demo.dto.auth.TokenDto;
import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
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
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE) // <--- ADD THIS LINE
    @ResponseBody() // Tells Spring to use a Message Converter
//    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public TokenDto login(@RequestBody LoginDto loginDto) {
        // ...
        return authService.login(loginDto); // Returns a Java object
    }


}
