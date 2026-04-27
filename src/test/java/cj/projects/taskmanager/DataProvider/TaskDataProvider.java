package cj.projects.taskmanager.DataProvider;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import cj.projects.taskmanager.services.dto.response.AuthorTaskDto;
import cj.projects.taskmanager.services.dto.response.TaskDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TaskDataProvider {

    private static TaskEntity taskEntity1;
    private static TaskEntity taskEntity2;
    private static TaskEntity taskEntity3;
    private static TaskDto taskDto1;
    private static TaskDto taskDto2;
    private static TaskDto taskDto3;
    private static UserEntity userEntity;
    private static AuthorTaskDto authorTaskDto;

    static{

        userEntity = UserDataProvider.getUser();
        taskEntity1= TaskEntity.builder()
                .id(UUID.randomUUID())
                .title("Tarea para test 1")
                .status(Status.NEW)
                .description("Tarea de prueba para test unitario")
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.of(22,22))
                .author(userEntity)
                .build();
        taskEntity2= TaskEntity.builder()
                .id(UUID.randomUUID())
                .title("Tarea para test 2")
                .status(Status.NEW)
                .description("Tarea de prueba para test unitario")
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.of(22,22))
                .author(userEntity)
                .build();
        taskEntity3= TaskEntity.builder()
                .id(UUID.randomUUID())
                .title("Tarea para test 3")
                .status(Status.NEW)
                .description("Tarea de prueba para test unitario")
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.of(22,22))
                .author(userEntity)
                .build();
        AuthorTaskDto author= new AuthorTaskDto();
        author.setName(userEntity.getName());
        author.setLastName(userEntity.getLastName());
        author.setUsername(userEntity.getUsername());
        author.setEmail(userEntity.getEmail());

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm");

        taskDto1= new TaskDto();
        taskDto1.setTitle(taskEntity1.getTitle());
        taskDto1.setDescription(taskEntity1.getDescription());
        taskDto1.setId(taskEntity1.getId());
        taskDto1.setStatus(taskEntity1.getStatus().name());
        taskDto1.setAuthor(author);
        taskDto1.setCreateAd(taskEntity1.getCreateAd().format(formatDate));
        taskDto1.setTimeCreateAd(taskEntity1.getCreateAdTime().format(formatTime));

        taskDto2 =new TaskDto();
        taskDto2.setTitle(taskEntity2.getTitle());
        taskDto2.setDescription(taskEntity2.getDescription());
        taskDto2.setId(taskEntity2.getId());
        taskDto2.setStatus(taskEntity2.getStatus().name());
        taskDto2.setAuthor(author);
        taskDto2.setCreateAd(taskEntity2.getCreateAd().format(formatDate));
        taskDto2.setTimeCreateAd(taskEntity2.getCreateAdTime().format(formatTime));

        taskDto3= new TaskDto();
        taskDto3.setTitle(taskEntity3.getTitle());
        taskDto3.setDescription(taskEntity3.getDescription());
        taskDto3.setId(taskEntity3.getId());
        taskDto3.setStatus(taskEntity3.getStatus().name());
        taskDto3.setAuthor(author);
        taskDto3.setCreateAd(taskEntity3.getCreateAd().format(formatDate));
        taskDto3.setTimeCreateAd(taskEntity3.getCreateAdTime().format(formatTime));
    }

    public static TaskEntity getTaskEntity1() {
        return taskEntity1;
    }

    public static TaskEntity getTaskEntity2() {
        return taskEntity2;
    }

    public static TaskEntity getTaskEntity3() {
        return taskEntity3;
    }

    public static TaskDto getTaskDto1() {
        return taskDto1;
    }

    public static TaskDto getTaskDto2() {
        return taskDto2;
    }

    public static TaskDto getTaskDto3() {
        return taskDto3;
    }

    public static UserEntity getUserEntity() {
        return userEntity;
    }

    public static AuthorTaskDto getAuthorTaskDto() {
        return authorTaskDto;
    }
}
