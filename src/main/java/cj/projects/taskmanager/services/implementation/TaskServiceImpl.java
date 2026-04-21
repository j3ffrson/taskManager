package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import cj.projects.taskmanager.persistence.repositories.TaskRepository;
import cj.projects.taskmanager.services.TaskService;
import cj.projects.taskmanager.services.dto.request.TaskRequest;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import cj.projects.taskmanager.services.dto.response.AuthorTaskDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Page<TaskDto> findAllTaskPage(Pageable pageable) {
        Page<TaskEntity> tasks = taskRepository.findAll(pageable);
        return tasks.map(TaskServiceImpl::getTaskDto);
    }

    @Override
    public Page<TaskDto> findAllTaskByStatusPage(String status,Pageable pageable) {
        Page<TaskEntity> tasks = taskRepository.findTaskEntitiesByStatus(Status.valueOf(status),pageable);
        return tasks.map(TaskServiceImpl::getTaskDto);
    }

    @Override
    public Page<TaskDto> findAllTaskByAuthorPage(Pageable pageable, UUID id) {
        Page<TaskEntity> tasks = taskRepository.findTaskEntitiesByAuthorId(id,pageable);
        return tasks.map(TaskServiceImpl::getTaskDto);
    }

    @Override
    public Page<TaskDto> findAllTaskByCreateAdDateBetween(LocalDate createAdDateAfter, LocalDate createAdDateBefore, Pageable pageable) {
        Page<TaskEntity> tasks = taskRepository.findTaskEntitiesByCreateAdDateBetween(createAdDateAfter,createAdDateBefore,pageable);
        return tasks.map(TaskServiceImpl::getTaskDto);
    }

    @Override
    public TaskDto findTaskById(UUID id) {

        TaskEntity task = taskRepository.findById(id).orElseThrow();
        return getTaskDto(task);
    }

    @Override
    public TaskDto createNewTask(TaskRequest taskRequest) {
        Authentication context= SecurityContextHolder.getContext().getAuthentication();
        TaskEntity newTask = TaskEntity.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(Status.NEW)
                .createAd(LocalDate.now().atStartOfDay())
                .author(UserEntity.builder().build())
                .build();
        taskRepository.save(newTask);
        return getTaskDto(newTask);
    }

    @Override
    public TaskDto updateNewTask(TaskRequest taskRequest,UUID id) {

        TaskEntity task= taskRepository.findById(id).orElseThrow();
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setStatus(Status.NEW);
        task.setUpdateAd(LocalDate.now().atStartOfDay());
        taskRepository.save(task);
        return getTaskDto(task);

    }

    @Override
    public Optional<Void> deleteTaskById(UUID id) {
        taskRepository.deleteById(id);
        return Optional.empty();
    }

    private static @NonNull AuthorTaskDto getUserDto(TaskEntity task) {
        UserEntity userEntity= task.getAuthor();
        AuthorTaskDto userDto = new AuthorTaskDto();
        userDto.setName(userEntity.getName());
        userDto.setUsername(userEntity.getUsername());
        userDto.setLastName(userEntity.getLastName());
        userDto.setEmail(userEntity.getEmail());
        return userDto;
    }
    private static @NonNull TaskDto getTaskDto(TaskEntity task) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        TaskDto dto = new TaskDto();
        dto.setUuid(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setAuthor(getUserDto(task));
        dto.setStatus(task.getStatus().name());
        dto.setCreateAd(task.getCreateAd().format(format));
        dto.setUpdateAd(task.getUpdateAd() != null ? task.getUpdateAd().format(format) : null);
        dto.setDeleteAd(task.getDeleteAd() != null ? task.getDeleteAd().format(format) : null);

        return dto;
    }
}
