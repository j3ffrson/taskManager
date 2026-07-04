package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.DataProvider.RoleDataProvider;
import cj.projects.taskmanager.DataProvider.UserDataProvider;
import cj.projects.taskmanager.persistence.entities.RoleEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.RoleRepository;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.dto.request.AuthLoginRequest;
import cj.projects.taskmanager.services.dto.response.AuthResponse;
import cj.projects.taskmanager.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername() {

        UserEntity userTest= UserDataProvider.getUser();
        RoleEntity role= RoleDataProvider.roleAdmin();
        userTest.setRoles(Set.of(role));

        String username= "jeffer";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userTest));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("CREATE","DELETE","READ","ROLE_ADMIN","UPDATE");


    }
    @Test
    void testLoadUserByUsernameNotFoundException() {
        String username= "jeffer";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThatThrownBy(()->userDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class);
    }
    @Test
    void loginUser() {

        String username= "jeffer";
        UserEntity userTest= UserDataProvider.getUser();
        RoleEntity role= RoleDataProvider.roleAdmin();
        userTest.setRoles(Set.of(role));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userTest));
        when(passwordEncoder.matches("milluh123",userTest.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(any(Authentication.class))).thenReturn("fake-token");

        AuthLoginRequest authLoginRequest = new AuthLoginRequest("jeffer","milluh123");

        AuthResponse authResponse = userDetailsService.loginUser(authLoginRequest);
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.username()).isEqualTo(authLoginRequest.username());
        assertThat(authResponse.JWT()).isEqualTo("fake-token");
        assertThat(authResponse.status()).isTrue();

        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).matches("milluh123",userTest.getPassword());
        verify(jwtUtil).generateToken(any(Authentication.class));



    }

    @Test
    void createUser() {
    }

    @Test
    void createOAuth2User() {
    }
}