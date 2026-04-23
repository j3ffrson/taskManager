package cj.projects.taskmanager.services.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthorTaskDto {

    private String name;
    private String username;
    private String lastName;
    private String email;

}
