package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import cj.projects.taskmanager.persistence.repositories.TaskRepository;
import cj.projects.taskmanager.services.TaskService;
import cj.projects.taskmanager.services.dto.request.TaskRequest;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import cj.projects.taskmanager.services.dto.response.UserDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return tasks.map(task -> {

            UserDto userDto = getUserDto(task);

            TaskDto dto = new TaskDto();
            dto.setUuid(task.getUuid());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setAuthor(userDto);
            dto.setStatus(task.getStatus().name());
            dto.setCreateAd(task.getCreateAd().format(format));
            dto.setUpdateAd(task.getUpdateAd() != null ? task.getUpdateAd().format(format) : null);
            dto.setDeleteAd(task.getDeleteAd() != null ? task.getDeleteAd().format(format) : null);
            return dto;
        });
    }

    @Override
    public Page<TaskDto> findAllTaskByStatusPage(String status,Pageable pageable) {
        Page<TaskEntity> tasks = taskRepository.findTaskEntitiesByStatus(Status.valueOf(status),pageable);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return tasks.map(task -> {

            UserDto userDto = getUserDto(task);

            TaskDto dto = new TaskDto();
            dto.setUuid(task.getUuid());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setAuthor(userDto);
            dto.setStatus(task.getStatus().name());
            dto.setCreateAd(task.getCreateAd().format(format));
            dto.setUpdateAd(task.getUpdateAd() != null ? task.getUpdateAd().format(format) : null);
            dto.setDeleteAd(task.getDeleteAd() != null ? task.getDeleteAd().format(format) : null);
            return dto;
        });
    }

    @Override
    public Page<TaskDto> findAllTaskByAuthorPage(Pageable pageable, UUID id) {
        Page<TaskEntity> tasks = taskRepository.findTaskEntitiesByAuthorUuid(id,pageable);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return tasks.map(task -> {

            UserDto userDto = getUserDto(task);

            TaskDto dto = new TaskDto();
            dto.setUuid(task.getUuid());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setAuthor(userDto);
            dto.setStatus(task.getStatus().name());
            dto.setCreateAd(task.getCreateAd().format(format));
            dto.setUpdateAd(task.getUpdateAd() != null ? task.getUpdateAd().format(format) : null);
            dto.setDeleteAd(task.getDeleteAd() != null ? task.getDeleteAd().format(format) : null);
            return dto;
        });
    }

    @Override
    public Page<TaskDto> findAllTaskByCreateAdDateBetween(LocalDate createAdDateAfter, LocalDate createAdDateBefore, Pageable pageable) {
        Page<TaskEntity> tasks = taskRepository.findTaskEntitiesByCreateAdDateBetween(createAdDateAfter,createAdDateBefore,pageable);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return tasks.map(task -> {

            UserDto userDto = getUserDto(task);

            TaskDto dto = new TaskDto();
            dto.setUuid(task.getUuid());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setAuthor(userDto);
            dto.setStatus(task.getStatus().name());
            dto.setCreateAd(task.getCreateAd().format(format));
            dto.setUpdateAd(task.getUpdateAd() != null ? task.getUpdateAd().format(format) : null);
            dto.setDeleteAd(task.getDeleteAd() != null ? task.getDeleteAd().format(format) : null);
            return dto;
        });
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

    private static @NonNull UserDto getUserDto(TaskEntity task) {
        UserEntity userEntity= task.getAuthor();
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setName(userEntity.getName());
        userDto.setUsername(userEntity.getUsername());
        userDto.setLastName(userEntity.getLastName());
        userDto.setEmail(userEntity.getEmail());
        userDto.setAccountNonExpired(userEntity.isAccountNonExpired());
        userDto.setAccountNonLocked(userEntity.isAccountNonLocked());
        userDto.setCredentialsNonExpired(userEntity.isCredentialsNonExpired());
        userDto.setEnabled(userEntity.isEnabled());
        return userDto;
    }
}
