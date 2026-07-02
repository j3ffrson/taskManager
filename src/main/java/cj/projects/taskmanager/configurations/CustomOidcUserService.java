package cj.projects.taskmanager.configurations;

import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.implementation.UserDetailsServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public CustomOidcUserService(@Lazy UserDetailsServiceImpl userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseGet(() -> userDetailsService.createOAuth2User(oidcUser));

        return new CustomOidcUser(oidcUser, userEntity);
    }
}
