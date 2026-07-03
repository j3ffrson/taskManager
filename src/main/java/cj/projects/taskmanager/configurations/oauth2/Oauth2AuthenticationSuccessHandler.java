package cj.projects.taskmanager.configurations.oauth2;

import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        UserEntity user;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomOidcUser customOidcUser) {
            user = customOidcUser.getUserEntity();
        } else if (principal instanceof CustomOauth2User customOauth2User) {
            user = customOauth2User.getUserEntity();
        } else {
            throw new IllegalStateException("Unknown principal type: " + principal.getClass());
        }

        List<GrantedAuthority> authorities= new ArrayList<>();

        user.getRoles().stream().forEach(role->
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName().name())));

        user.getRoles().stream().flatMap(role->role.getListaPermisos().stream())
                .forEach(permission->authorities.add(new SimpleGrantedAuthority(permission.getName())));

        UsernamePasswordAuthenticationToken userToken= new UsernamePasswordAuthenticationToken(user.getUsername(),null,authorities);

        String token= jwtUtil.generateToken(userToken);

        // 5. Responder JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = """
        {
            "token": "%s",
            "email": "%s",
            "roles": %s
        }
        """.formatted(
                token,
                user.getEmail(),
                user.getRoles().stream()
                        .map(r -> "\"" + r.getName().name() + "\"")
                        .toList()
        );

        response.getWriter().write(json);

    }
}
