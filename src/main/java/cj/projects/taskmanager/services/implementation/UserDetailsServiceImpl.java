package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.dto.request.AuthLoginRequest;
import cj.projects.taskmanager.services.dto.response.AuthResponse;
import cj.projects.taskmanager.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user= userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
        List<SimpleGrantedAuthority> authorities= new ArrayList<>();

        user.getRoles().forEach(roles->{
            authorities.add(new SimpleGrantedAuthority(roles.getName().name()));
        });

        user.getRoles().stream()
                .flatMap(role->role.getListaPermisos().stream())
                .forEach(permission->authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(user.getUsername(),user.getPassword(),authorities);
    }

    public AuthResponse loginUser(@Valid AuthLoginRequest authLoginRequest){

        String username= authLoginRequest.username();
        String password= authLoginRequest.password();

        Authentication authentication= authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token= jwtUtil.generateToken(authentication);
        List<String> roles= authentication.getAuthorities().stream().map(Objects::toString).toList();

        return new AuthResponse(
                username,
                roles.stream().filter(role->role.startsWith("ROLE_")).toList()
                        .stream().map(role->role.replace("ROLE_",",")).toList(),
                token,
                true
        );

    }

    private Authentication authenticate(String username,String password){

        UserDetails userDetails=loadUserByUsername(username);

        if(!passwordEncoder.matches(password,userDetails.getPassword())) throw new BadCredentialsException("Invalid credentials");

        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
    }

}
