package cj.projects.taskmanager.persistence.services.dto;

import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TaskDto {

    private UUID uuid;
    private String title;
    private String description;
    private LocalDateTime createAd;
    private LocalDateTime updateAd;
    private LocalDateTime deleteAd;

    private Status status;

    private UserEntity author;

}
