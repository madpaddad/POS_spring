package com.example.demo.config;


import com.example.demo.auth.AuthService;
import jakarta.servlet.DispatcherType;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ComponentScan("com")
public class MyAppConfig {


    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
//    private final SecurityAuthenticationFilter securityAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;

    private final JwtService jwtService;
    public MyAppConfig(AuthService authService, PasswordEncoder passwordEncoder, RateLimitFilter rateLimitFilter, JwtService jwtService) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
//        this.securityAuthenticationFilter = securityAuthenticationFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.jwtService = jwtService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(authService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(jwtService);
    }

    // Filter requests match with pattern
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        AuthenticationFilter filter = context.getBean(AuthenticationFilter.class);

        http.
                authorizeHttpRequests((requests) -> requests
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/error", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter, AuthenticationFilter.class)
                .csrf(crsf -> crsf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public RouteLocator myRoutes(RouteLocatorBuilder builder){
//        return builder.routes().build();
//    }

}
