package com.example.demo.config;


import com.example.demo.auth.AuthController;
import com.example.demo.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.AuthenticationEntryPoint;
@Configuration
//@EnableWebMvc
@EnableWebSecurity
@EnableMethodSecurity
@ComponentScan("com")
public class MyAppConfig {

//    @Autowired
//    private final AccessDeniedHandler accessDeniedHandler;
//
//    @Autowired
//    private final AuthenticationEntryPoint authenticationEntryPoint;

//    @Autowired
    private final AuthService authService;

    public MyAppConfig(AuthService authService) {
        this.authService = authService;
    }


    @Bean
    InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(authService);
        provider.setPasswordEncoder();
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
                .userDetailsService(authService)
                .authenticationProvider(authenticationProvider())
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(
//                        customizer ->
//                                customizer
//                                        .accessDeniedHandler(accessDeniedHandler)
//                                        .authenticationEntryPoint(authenticationEntryPoint))
//                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}
