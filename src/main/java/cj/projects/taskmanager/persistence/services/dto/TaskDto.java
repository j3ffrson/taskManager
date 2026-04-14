package cj.projects.taskmanager.persistence.services.dto;

import cj.projects.taskmanager.persistence.entities.enums.Status;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskDto {

    private UUID uuid;
    private String title;
    private String description;
    private String createAd;
    private String updateAd;
    private String deleteAd;

    private Status status;

    private UserDto author;

}
