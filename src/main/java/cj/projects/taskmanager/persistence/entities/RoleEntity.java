package cj.projects.taskmanager.persistence.entities;

import cj.projects.taskmanager.persistence.entities.enums.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "role")
@Entity
public class RoleEntity {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private Roles name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> listaPermisos = new HashSet<>();
}
