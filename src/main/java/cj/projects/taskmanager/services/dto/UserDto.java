package cj.projects.taskmanager.services.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String username;
    private String lastName;
    private String email;

    private boolean isEnabled;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

}
