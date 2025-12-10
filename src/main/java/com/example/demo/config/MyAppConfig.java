package com.example.demo.config;


import com.example.demo.auth.AuthService;
import jakarta.servlet.DispatcherType;
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

//    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    String introspectionUri;

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityAuthenticationFilter securityAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;

    private final JwtService jwtService;
    public MyAppConfig(AuthService authService, PasswordEncoder passwordEncoder, SecurityAuthenticationFilter securityAuthenticationFilter, RateLimitFilter rateLimitFilter, JwtService jwtService) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.securityAuthenticationFilter = securityAuthenticationFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.jwtService = jwtService;
    }


//    @Bean
//    InternalResourceViewResolver viewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/views/");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(authService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityAuthenticationFilter jwtAuthenticationFilter() {
        return new SecurityAuthenticationFilter(jwtService);
    }

    // Filter requests match with pattern
    // if fail => go back to login then use AuthenticationProvider with our authSerivce
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests((requests) -> requests
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/error", "/login", "/api/Hi", "/api/file-service/**", "/api/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken
//                    (token -> token.introspectionUri(this.introspectionUri)
//                        .introspectionClientCredentials(this.clientId, this.clientSecret)))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter, SecurityAuthenticationFilter.class)
                .csrf(crsf -> crsf.disable())
//                .formLogin(Customizer.withDefaults())
//                .exceptionHandling(
//                        customizer ->
//                                customizer
//                                        .accessDeniedHandler(accessDeniedHandler)
//                                        .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                .logout((logout) -> logout.permitAll());

        return http.build();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
