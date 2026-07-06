package cj.projects.taskmanager.services.oauth2;

import cj.projects.taskmanager.persistence.entities.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOauth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    @Getter
    private final UserEntity userEntity;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities= new ArrayList<>();

        userEntity.getRoles().stream().forEach(role->
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName().name())));

        userEntity.getRoles().stream().flatMap(role->role.getListaPermisos().stream())
                .forEach(permission->authorities.add(new SimpleGrantedAuthority(permission.getName())));


        return authorities;
    }

    @Override
    public String getName() {
        return userEntity.getUsername();
    }
}
