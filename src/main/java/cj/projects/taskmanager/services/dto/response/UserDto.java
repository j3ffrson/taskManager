package cj.projects.taskmanager.services.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID id;
    private String name;
    private String username;
    private String lastName;
    private String email;

    private boolean isEnabled;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

}
