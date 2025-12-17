package com.example.demo.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;


//@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
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
//            return false;
    }

//    @Override
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("DoFilterInternal hit");

        String authenticationHeader = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken auth = null;

        log.info("authentication header{}", authenticationHeader);
        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            String token = authenticationHeader.substring(7);

            try{
                log.info("token {}", token);
                AuthUser authuser = jwtService.resolveJwtToken(token);

                log.info("Authuser {}", authuser);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    auth = new UsernamePasswordAuthenticationToken(
                            authuser.getId(),
                            null,
                            List.of()
                    );
                }
            } catch (TokenExpiredException ex) {
                log.warn("JWT token expired: {}", ex.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"JWT token has expired\"}");
                return; // stop further filters
            } catch (JWTVerificationException ex) {
                log.warn("JWT token invalid: {}", ex.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"JWT token is invalid\"}");
                return;
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
