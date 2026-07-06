package cj.projects.taskmanager.services.oauth2;

import cj.projects.taskmanager.DataProvider.UserDataProvider;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOauth2UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Spy
    @InjectMocks
    CustomOauth2UserService customOauth2UserService;

    @Test
    void shouldLoadUserWhenEmailExist() {

        UserEntity userEntity= UserDataProvider.getUser();
        OAuth2User oAuth2User = Mockito.mock(OAuth2User.class);

        doReturn(oAuth2User).when(customOauth2UserService).fetchOAuth2User(any());

        when(oAuth2User.getAttribute("email")).thenReturn("chaustrejefferson@gmail.com");
        when(userRepository.findByEmail("chaustrejefferson@gmail.com")).thenReturn(Optional.of(userEntity));

        OAuth2User result= customOauth2UserService.loadUser(mock(OAuth2UserRequest.class));

        verify(userRepository).findByEmail("chaustrejefferson@gmail.com");
        verify(userDetailsService,never()).createUser(any());
        assertThat(result).isInstanceOf(CustomOauth2User.class);
    }
    @Test
    void shouldCreateNewUserWhenEmailNotExist() {

        // Arrange
        UserEntity userEntity = UserDataProvider.getUser();
        OAuth2User oAuth2User = mock(OAuth2User.class);

        doReturn(oAuth2User)
                .when(customOauth2UserService)
                .fetchOAuth2User(any());

        when(oAuth2User.getAttribute("email"))
                .thenReturn("chaustrejefferson@gmail.com");

        when(userRepository.findByEmail("chaustrejefferson@gmail.com"))
                .thenReturn(Optional.empty());

        when(userDetailsService.createOAuth2User(oAuth2User))
                .thenReturn(userEntity);

        // Act
        OAuth2User result =
                customOauth2UserService.loadUser(mock(OAuth2UserRequest.class));

        // Assert
        verify(userRepository)
                .findByEmail("chaustrejefferson@gmail.com");

        verify(userDetailsService)
                .createOAuth2User(oAuth2User);

        assertThat(result)
                .isInstanceOf(CustomOauth2User.class);

        CustomOauth2User customUser = (CustomOauth2User) result;

        assertThat(customUser.getUserEntity())
                .isEqualTo(userEntity);
    }
}