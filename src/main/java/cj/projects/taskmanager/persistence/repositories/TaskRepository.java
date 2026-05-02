package cj.projects.taskmanager.persistence.repositories;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    Page<TaskEntity> findTaskEntitiesByStatus(Status status, Pageable pageable);

    @Query("SELECT t FROM TaskEntity t JOIN FETCH t.author WHERE t.author = :author")
    Page<TaskEntity> findTaskEntitiesByAuthor(@Param("author") UserEntity author, Pageable pageable);

    Page<TaskEntity> findTaskEntitiesByCreateAdBetween(LocalDate createAdAfter, LocalDate createAdBefore, Pageable page);

}
