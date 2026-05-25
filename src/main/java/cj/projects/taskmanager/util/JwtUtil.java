package cj.projects.taskmanager.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.private.secret.key}")
    private String secretKey;
    @Value("${spring.security.jwt.user.generator}")
    private String userGenerator;

}
