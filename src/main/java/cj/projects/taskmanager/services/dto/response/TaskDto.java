package cj.projects.taskmanager.services.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskDto {

    private UUID uuid;
    private String title;
    private String description;
    private String createAd;
    private String timeCreateAd;
    private String updateAd;
    private String timeUpdateAd;

    private String status;

    private AuthorTaskDto author;

}
