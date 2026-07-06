package cj.projects.taskmanager.services.oauth2;

import cj.projects.taskmanager.persistence.entities.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOidcUser implements OidcUser {

    private final OidcUser oidcUser;
    @Getter
    private final UserEntity userEntity;

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
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
