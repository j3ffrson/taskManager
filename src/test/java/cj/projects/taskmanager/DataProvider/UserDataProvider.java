package cj.projects.taskmanager.DataProvider;

import cj.projects.taskmanager.persistence.entities.PermissionEntity;
import cj.projects.taskmanager.persistence.entities.RoleEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Roles;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserDataProvider {

    public static UserEntity getUser() {
        PermissionEntity create = PermissionEntity.builder().name("CREATE").build();
        PermissionEntity read = PermissionEntity.builder().name("READ").build();
        PermissionEntity update = PermissionEntity.builder().name("UPDATE").build();
        PermissionEntity delete = PermissionEntity.builder().name("DELETE").build();

        Set<PermissionEntity> permissions = new HashSet<>();
        permissions.add(create);
        permissions.add(read);
        permissions.add(update);
        permissions.add(delete);

        RoleEntity adminRole = RoleEntity.builder()
                .name(Roles.USER)
                .listaPermisos(permissions)
                .build();

        return UserEntity.builder()
                .name("Jefferson")
                .lastName("Chaustre")
                .username("jeffer")
                .email("chaustrejefferson@gmail.com")
                .password("passtest")
                .roles(Set.of(adminRole))
                .tasks(Collections.emptyList())
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .build();
    }
}
