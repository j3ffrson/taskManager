package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


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
}
