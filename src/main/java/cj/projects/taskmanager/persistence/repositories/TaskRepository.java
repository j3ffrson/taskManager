package cj.projects.taskmanager.persistence.repositories;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    Page<TaskEntity> findTaskEntitiesByStatus(Status status, Pageable pageable);
    Page<TaskEntity> findTaskEntitiesByAuthor(UserEntity author, Pageable pageable);
    Page<TaskEntity> findTaskEntitiesByCreateAdDateBetween(LocalDate createAdDateAfter, LocalDate createAdDateBefore, Pageable pageable);
}
