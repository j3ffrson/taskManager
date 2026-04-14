package cj.projects.taskmanager.persistence.repositories;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity> findTaskEntitiesByStatus(Status status);
    List<TaskEntity> findTaskEntitiesByAuthorUuid(UUID authorUuid);
    List<TaskEntity> findTaskEntitiesByCreateAdDateBetween(LocalDate createAdDateAfter, LocalDate createAdDateBefore);
}
