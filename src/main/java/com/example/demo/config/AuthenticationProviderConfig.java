//package com.example.demo.config;
//
//import com.example.demo.auth.AuthService;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AuthenticationProviderConfig implements AuthenticationManager {
//
//    public AuthenticationProviderConfig(AuthService authService, PasswordEncoder passwordEncoder) {
//        this.authService = authService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    private final AuthService authService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//
////        UserResponseWithCredentials userCredentialsByUsername =
////                userService.getUserCredentialsByUsername(authentication.getName());
////
////
////        if (!passwordEncoder.matches(
////                authentication.getCredentials().toString(), userCredentialsByUsername.passwordHash())) {
////            throw new ApplicationAuthenticationException("Bad credentials");
////        }
////
////
////        AuthUser authUser =
////                new AuthUser(
////                        userCredentialsByUsername.userResponse().id(),
////                        userCredentialsByUsername.userResponse().roles(),
////                        userCredentialsByUsername.passwordHash());
////
////
////        return new UsernamePasswordAuthenticationToken(
////                authUser, authentication.getCredentials(), authUser.getAuthorities());
//    }
//}
