package cj.projects.taskmanager.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.private.secret.key}")
    private String secretKey;
    @Value("${spring.security.jwt.user.generator}")
    private String userGenerator;

    public String generateToken(Authentication authentication){
        Algorithm algorithm= Algorithm.HMAC256(secretKey);
        String username= authentication.getName();
        String permmissions= authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("permissions", permmissions)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+ TimeUnit.HOURS.toMillis(3)))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);

    }

}
