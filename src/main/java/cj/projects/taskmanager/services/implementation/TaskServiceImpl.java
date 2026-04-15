package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.services.TaskService;
import cj.projects.taskmanager.services.dto.request.TaskRequest;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class TaskServiceImpl implements TaskService {

    @Override
    public Page<TaskDto> findAllTaskPage(Pageable pageable) {
        return null;
    }

    @Override
    public Page<TaskDto> findAllTaskByStatusPage(Pageable pageable) {
        return null;
    }

    @Override
    public Page<TaskDto> findAllTaskByAuthorPage(Pageable pageable, UUID id) {
        return null;
    }

    @Override
    public Page<TaskDto> findAllTaskByCreateAdDateBetween(LocalDate createAdDateAfter, LocalDate createAdDateBefore, Pageable pageable) {
        return null;
    }

    @Override
    public TaskDto findTaskById(UUID id) {
        return null;
    }

    @Override
    public TaskDto createNewTask(TaskRequest taskRequest) {
        return null;
    }

    @Override
    public TaskDto updateNewTask(TaskRequest taskRequest) {
        return null;
    }

    @Override
    public Optional<Void> deleteTaskById(UUID id) {
        return Optional.empty();
    }
}
