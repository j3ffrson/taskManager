package cj.projects.taskmanager.DataProvider;

import cj.projects.taskmanager.persistence.entities.PermissionEntity;
import cj.projects.taskmanager.persistence.entities.RoleEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Roles;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

public class UserDataProvider {

    private static PermissionEntity create;
    private static PermissionEntity read;
    private static PermissionEntity update;
    private static PermissionEntity delete;
    private static RoleEntity admin;
    private static UserEntity user;

    static{
        create= PermissionEntity.builder().name("CREATE").build();
        read= PermissionEntity.builder().name("READ").build();
        update= PermissionEntity.builder().name("UPDATE").build();
        delete= PermissionEntity.builder().name("DELETE").build();

        admin= RoleEntity.builder()
                .name(Roles.USER)
                .listaPermisos(Set.of(create,read,update,delete))
                .build();

        user= UserEntity.builder()
                .name("Jefferson")
                .lastName("Chaustre")
                .username("jeffer")
                .email("chaustrejefferson@gmail.com")
                .password("passtest")
                .roles(Set.of(admin))
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .build();
    }

    public static PermissionEntity getCreate() {
        return create;
    }

    public static PermissionEntity getRead() {
        return read;
    }

    public static PermissionEntity getUpdate() {
        return update;
    }

    public static RoleEntity getAdmin() {
        return admin;
    }

    public static PermissionEntity getDelete() {
        return delete;
    }

    public static UserEntity getUser() {
        return user;
    }
}
