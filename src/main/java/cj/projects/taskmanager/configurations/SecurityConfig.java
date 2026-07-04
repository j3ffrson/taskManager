package cj.projects.taskmanager.configurations;


import cj.projects.taskmanager.configurations.oauth2.Oauth2AuthenticationSuccessHandler;
import cj.projects.taskmanager.services.CustomOauth2UserService;
import cj.projects.taskmanager.services.CustomOidcUserService;
import cj.projects.taskmanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthEntryPoint authEntryPoint;
    private final Oauth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomOauth2UserService customOauth2UserService;
    private final CustomOidcUserService customOidcUserService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2UserService)
                                .oidcUserService(customOidcUserService)
                        )
                )
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(authEntryPoint)
                )
                .addFilterBefore(
                        new TokenValidator(jwtUtil, authEntryPoint),
                        BasicAuthenticationFilter.class
                )
                .build();
    }

}
