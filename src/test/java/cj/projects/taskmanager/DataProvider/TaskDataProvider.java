package cj.projects.taskmanager.DataProvider;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import cj.projects.taskmanager.services.dto.request.TaskRequest;
import cj.projects.taskmanager.services.dto.response.AuthorTaskDto;
import cj.projects.taskmanager.services.dto.response.TaskDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TaskDataProvider {

    private static final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm");

    public static TaskEntity getTaskEntity1() {
        return TaskEntity.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .title("Tarea para test 1")
                .status(Status.NEW)
                .description("Tarea de prueba para test unitario")
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.of(22,22))
                .author(UserDataProvider.getUser())
                .build();
    }

    public static TaskEntity getTaskEntity2() {
        return TaskEntity.builder()
                .id(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .title("Tarea para test 2")
                .status(Status.NEW)
                .description("Tarea de prueba para test unitario")
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.of(22,22))
                .author(UserDataProvider.getUser())
                .build();
    }

    public static TaskEntity getTaskEntity3() {
        return TaskEntity.builder()
                .id(UUID.fromString("33333333-3333-3333-3333-333333333333"))
                .title("Tarea para test 3")
                .status(Status.STARTING)
                .description("Tarea de prueba para test unitario")
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.of(22,22))
                .author(UserDataProvider.getUser())
                .build();
    }

    public static TaskDto getTaskDto1() {
        return mapToDto(getTaskEntity1());
    }

    public static TaskDto getTaskDto2() {
        return mapToDto(getTaskEntity2());
    }

    public static TaskDto getTaskDto3() {
        return mapToDto(getTaskEntity3());
    }

    public static UserEntity getUserEntity() {
        return UserDataProvider.getUser();
    }

    public static AuthorTaskDto getAuthorTaskDto() {
        UserEntity user = UserDataProvider.getUser();
        AuthorTaskDto author = new AuthorTaskDto();
        author.setName(user.getName());
        author.setLastName(user.getLastName());
        author.setUsername(user.getUsername());
        author.setEmail(user.getEmail());
        return author;
    }

    public static TaskRequest getTaskRequest() {
        return new TaskRequest("Nueva tarea de prueba", "Descripcion de la nueva tarea de prueba", "NEW");
    }

    private static TaskDto mapToDto(TaskEntity entity) {
        TaskDto dto = new TaskDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus().name());
        dto.setCreateAd(entity.getCreateAd().format(formatDate));
        dto.setTimeCreateAd(entity.getCreateAdTime().format(formatTime));
        dto.setAuthor(getAuthorTaskDto());
        return dto;
    }
}
