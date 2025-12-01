package com.example.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final Algorithm signingAlgorithm;
    public JwtService(@Value("${jwt.signing-secret}") String signingSecret) {


        // this example uses a symmetric signature of the JWT token, but if you want the issuer and the
        // verifier of the JWT token to be different applications you may want to use an asymmetric
        // signature


        this.signingAlgorithm = Algorithm.HMAC256(signingSecret);
    }

    public AuthUser resolveJwtToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(signingAlgorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);


            String userId = decodedJWT.getSubject();
            String roleString = decodedJWT.getClaim("role").asString();

            Role role = Role.valueOf(roleString);
            return new AuthUser(userId, role);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("JWT is not valid");
        }
    }

    public String createJwtToken(AuthUser authUser) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 3600000; // 1 hour validity
        Date exp = new Date(expMillis);


        String roleName = authUser.getRole().name();

        return JWT.create()
                .withSubject(authUser.getId())
                .withClaim("role", roleName)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(signingAlgorithm);
    }
}
