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

        return UserEntity.builder()
                .name("Jefferson")
                .lastName("Chaustre")
                .username("jeffer")
                .email("chaustrejefferson@gmail.com")
                .password("passtest")
                .tasks(Collections.emptyList())
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .build();
    }
}
