package com.example.demo.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.auth.AuthController;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import io.github.bucket4j.Bandwidth;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.Refill;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Component
public class SecurityAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(SecurityAuthenticationFilter.class);

    private final JwtService jwtService;

    public SecurityAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private Bucket createNewBucket(String clientIp) {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1))); // 10 requests per minute
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip JWT check for login endpoint
        String path = request.getServletPath();
        boolean skip = path.equals("/login"); // adjust if context path exists
        return skip;
    }

//    @Override
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String authenticationHeader = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken auth = null;

        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            String token = authenticationHeader.substring(7);
            try {
                AuthUser authuser = jwtService.resolveJwtToken(token);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    auth = new UsernamePasswordAuthenticationToken(
                            authuser.getId(),
                            null,
                            List.of()
                    );
                }

            } catch (JWTVerificationException e) {
                return ;
            }
        }
        else {
             sendUnauthorized(response, "Invalid or Expired Token");
            return;
        }

        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // **This must be called** to continue the request chain
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }


    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = "{ \"error\": \"" + message + "\" }";
        response.getWriter().write(json);
    }


}
