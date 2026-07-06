package cj.projects.taskmanager.DataProvider;

import cj.projects.taskmanager.persistence.entities.PermissionEntity;
import cj.projects.taskmanager.persistence.entities.RoleEntity;
import cj.projects.taskmanager.persistence.entities.enums.Roles;

import java.util.HashSet;
import java.util.Set;

public class RoleDataProvider {

    public static RoleEntity roleAdmin(){
        PermissionEntity create = PermissionEntity.builder().name("CREATE").build();
        PermissionEntity read = PermissionEntity.builder().name("READ").build();
        PermissionEntity update = PermissionEntity.builder().name("UPDATE").build();
        PermissionEntity delete = PermissionEntity.builder().name("DELETE").build();

        Set<PermissionEntity> permissions = new HashSet<>();
        permissions.add(create);
        permissions.add(read);
        permissions.add(update);
        permissions.add(delete);

        return RoleEntity.builder()
                .name(Roles.ADMIN)
                .listaPermisos(permissions)
                .build();
    }

}
