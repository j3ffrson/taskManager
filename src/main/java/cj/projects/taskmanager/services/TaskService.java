package cj.projects.taskmanager.services;

import cj.projects.taskmanager.services.dto.request.TaskRequest;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {

    Page<TaskDto> findAllTaskPage(Pageable pageable);
    Page<TaskDto> findAllTaskByStatusPage(Pageable pageable);
    Page<TaskDto> findAllTaskByAuthorPage(Pageable pageable,UUID id);
    Page<TaskDto> findAllTaskByCreateAdDateBetween(LocalDate createAdDateAfter, LocalDate createAdDateBefore, Pageable pageable);
    TaskDto findTaskById(UUID id);
    TaskDto createNewTask(TaskRequest taskRequest);
    TaskDto updateNewTask(TaskRequest taskRequest);
    Optional<Void> deleteTaskById(UUID id);

}
