package com.example.demo.auth;

import com.example.demo.config.JwtService;
import com.example.demo.dto.auth.LoginDto;
import com.example.demo.dto.auth.TokenDto;
import com.example.demo.model.User;
import com.example.demo.config.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.EmptyStackException;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

//    private final AuthUserCache authUserCache;

    private final PasswordEncoder passwordEncoder;

    private final AuthRepository authRepository;

    private final JwtService jwtService;

    public AuthService(PasswordEncoder passwordEncoder, AuthRepository authRepository, JwtService jwtService){
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
        this.jwtService =  jwtService;
    }



//    Find by PhoneNumber not username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
            User user = this.authRepository.findByPhoneNumber(username);

//            log.info("looking for the user");


            if (user == null){
                throw new UsernameNotFoundException("User with phone number not found");
            }

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getPhone_number())  // use phone number as username
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
    }

//    @PostMapping("/api/login")
    @ResponseBody
    public TokenDto login(LoginDto loginDto){

        User user = this.authRepository.findByPhoneNumber(loginDto.getPhoneNumber());
        if (user == null){
            throw new UsernameNotFoundException("User with phone number not found");
        }
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        // String token = UUID.randomUUID().toString();
        AuthUser authUser = new AuthUser();
        authUser.setName(user.getName());
        authUser.setRole(user.getRole());
        
        String token = jwtService.createJwtToken(authUser);
        log.info(token);
        return new TokenDto(token);

    }

}
