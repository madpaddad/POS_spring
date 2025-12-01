package com.example.demo.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.auth.AuthController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(SecurityAuthenticationFilter.class);

    private final JwtService jwtService;

    public SecurityAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip JWT check for login endpoint
        String path = request.getServletPath();
        boolean skip = path.equals("/api/login"); // adjust if context path exists
        if (skip) {
            log.info("Skipping JWT filter for {}", path);
        }
        return skip;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        logger.info("I am already finding it. ");
        String authenticationHeader = request.getHeader("Authorization");
        log.info("why am I here");
        UsernamePasswordAuthenticationToken auth = null;
        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            String token = authenticationHeader.substring(7);
            try {
                AuthUser authuser = jwtService.resolveJwtToken(token);
                String userId = authuser.getId();
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    auth = new UsernamePasswordAuthenticationToken(authuser.getId(), null, List.of());
                }


            } catch (JWTVerificationException e) {
                // token invalid or expired
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }



}
