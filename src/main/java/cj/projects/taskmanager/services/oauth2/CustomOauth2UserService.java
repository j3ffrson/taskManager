package cj.projects.taskmanager.services.oauth2;

import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.implementation.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User= super.loadUser(userRequest);

        String email= oAuth2User.getAttribute("email");

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseGet(() -> userDetailsService.createOAuth2User(oAuth2User));

        return new CustomOauth2User(oAuth2User, userEntity);
    }
}
