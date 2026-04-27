package cj.projects.taskmanager.DataProvider;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import cj.projects.taskmanager.services.dto.response.AuthorTaskDto;
import cj.projects.taskmanager.services.dto.response.TaskDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class TaskDataProvider {

    private static TaskEntity taskEntity;
    private static TaskDto taskDto;
    private static UserEntity userEntity;
    private static AuthorTaskDto authorTaskDto;

    static{

        userEntity = UserDataProvider.getUser();
        taskEntity= TaskEntity.builder()
                .id(UUID.randomUUID())
                .title("Tarea para test 1")
                .status(Status.NEW)
                .description("Tarea de prueba para test de unitario")
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.now())
                .author(userEntity)
                .build();
        userEntity.getTasks().add(taskEntity);

        AuthorTaskDto author= new AuthorTaskDto();
        author.setName(userEntity.getName());
        author.setLastName(userEntity.getLastName());
        author.setUsername(userEntity.getUsername());
        author.setEmail(userEntity.getEmail());

        taskDto= new TaskDto();
        taskDto.setTitle(taskEntity.getTitle());
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setId(taskEntity.getId());
        taskDto.setStatus(taskEntity.getStatus().name());
        taskDto.setAuthor(author);
        taskDto.setCreateAd(taskEntity.getCreateAd().toString());
        taskDto.setTimeCreateAd(taskEntity.getCreateAdTime().toString());

    }

}
