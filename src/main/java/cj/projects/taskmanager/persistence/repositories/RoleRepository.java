package cj.projects.taskmanager.persistence.repositories;

import cj.projects.taskmanager.persistence.entities.RoleEntity;
import cj.projects.taskmanager.persistence.entities.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    Set<RoleEntity> findRoleEntitiesByNameIn(Collection<Roles> names);

}
