package cj.projects.taskmanager.services.oauth2;

import cj.projects.taskmanager.DataProvider.UserDataProvider;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomOidcUserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Spy
    @InjectMocks
    CustomOidcUserService customOidcUserService;

    @Test
    void shouldLoadUserWhenEmailExist() {

        UserEntity userEntity= UserDataProvider.getUser();
        OidcUser oidcUser = Mockito.mock(OidcUser.class);

        doReturn(oidcUser).when(customOidcUserService).fetchOidcUser(any());

        when(oidcUser.getAttribute("email")).thenReturn("chaustrejefferson@gmail.com");
        when(userRepository.findByEmail("chaustrejefferson@gmail.com")).thenReturn(Optional.of(userEntity));

        OidcUser result= customOidcUserService.loadUser(mock(OidcUserRequest.class));

        verify(userRepository).findByEmail("chaustrejefferson@gmail.com");
        verify(userDetailsService,never()).createUser(any());
        assertThat(result).isInstanceOf(CustomOidcUser.class);
    }
    @Test
    void shouldCreateNewUserWhenEmailNotExist() {

        // Arrange
        UserEntity userEntity = UserDataProvider.getUser();
        OidcUser oidc2User = mock(OidcUser.class);

        doReturn(oidc2User)
                .when(customOidcUserService)
                .fetchOidcUser(any());

        when(oidc2User.getAttribute("email"))
                .thenReturn("chaustrejefferson@gmail.com");

        when(userRepository.findByEmail("chaustrejefferson@gmail.com"))
                .thenReturn(Optional.empty());

        when(userDetailsService.createOAuth2User(oidc2User))
                .thenReturn(userEntity);

        // Act
        OidcUser result =
                customOidcUserService.loadUser(mock(OidcUserRequest.class));

        // Assert
        verify(userRepository)
                .findByEmail("chaustrejefferson@gmail.com");

        verify(userDetailsService)
                .createOAuth2User(oidc2User);

        assertThat(result)
                .isInstanceOf(CustomOidcUser.class);

        CustomOidcUser customUser = (CustomOidcUser) result;

        assertThat(customUser.getUserEntity())
                .isEqualTo(userEntity);
    }
}