package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import cj.projects.taskmanager.persistence.repositories.TaskRepository;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.TaskService;
import cj.projects.taskmanager.services.dto.request.TaskRequest;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import cj.projects.taskmanager.services.dto.response.AuthorTaskDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

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
    public Page<TaskDto> findAllTaskByAuthorPage(Pageable pageable) {
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        assert context != null;
        UserEntity author= userRepository.findByUsername(context.getName()).orElseThrow();
        Page<TaskEntity> tasks = taskRepository.findTaskEntitiesByAuthor(author,pageable);
        return tasks.map(TaskServiceImpl::getTaskDto);
    }

    @Override
    public Page<TaskDto> findAllTaskByCreateAdBetween(LocalDate createAdDateAfter, LocalDate createAdDateBefore, Pageable pageable) {
        Page<TaskEntity> tasks = taskRepository
                .findTaskEntitiesByCreateAdBetween(createAdDateAfter,createAdDateBefore,pageable);
        return tasks.map(TaskServiceImpl::getTaskDto);
    }

    @Override
    public TaskDto findTaskById(UUID id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow();
        return getTaskDto(task);
    }

    @Override
    @Transactional // <-- !IMPORTANTE
    public TaskDto createNewTask(TaskRequest taskRequest) {
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUsername(context.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + context.getName()));

        TaskEntity newTask = TaskEntity.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(Status.NEW)
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.now())
                .author(user)
                .build();

        TaskEntity savedTask = taskRepository.save(newTask);
        return getTaskDto(savedTask);
    }

    @Override
    @Transactional
    public TaskDto updateNewTask(TaskRequest taskRequest, UUID id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow();
        task.setTitle(taskRequest.title()==null?task.getTitle():taskRequest.title());
        task.setDescription(taskRequest.description()==null?task.getDescription():taskRequest.description());
        task.setStatus(taskRequest.status()==null?task.getStatus():Status.valueOf(taskRequest.status()));
        task.setUpdateAd(LocalDate.now());
        task.setUpdateAdTime(LocalTime.now());
        taskRepository.save(task);
        return getTaskDto(task);
    }

    @Override
    public Optional<Void> deleteTaskById(UUID id) {
        taskRepository.deleteById(id);
        return Optional.empty();
    }

    private static @Nullable AuthorTaskDto getUserDto(TaskEntity task) {
        UserEntity userEntity = task.getAuthor();
        if (userEntity == null) return null;

        AuthorTaskDto userDto = new AuthorTaskDto();
        userDto.setName(userEntity.getName());
        userDto.setUsername(userEntity.getUsername());
        userDto.setLastName(userEntity.getLastName());
        userDto.setEmail(userEntity.getEmail());
        return userDto;
    }

    private static @NonNull TaskDto getTaskDto(TaskEntity task) {
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm");
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setAuthor(getUserDto(task));
        dto.setStatus(task.getStatus() != null ? task.getStatus().name() : null);
        dto.setCreateAd(task.getCreateAd() != null ? task.getCreateAd().format(formatDate) : null);
        dto.setTimeCreateAd(task.getCreateAdTime() != null ? task.getCreateAdTime().format(formatTime) : null);
        dto.setUpdateAd(task.getUpdateAd() != null ? task.getUpdateAd().format(formatDate) : null);
        dto.setTimeUpdateAd(task.getUpdateAdTime()!=null?task.getUpdateAdTime().format(formatTime):null);
        return dto;
    }
}
