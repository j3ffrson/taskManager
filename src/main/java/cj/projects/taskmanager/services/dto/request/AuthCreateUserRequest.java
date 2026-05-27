package cj.projects.taskmanager.services.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
        @NotBlank String name,
        @NotBlank String lastName,
        @NotBlank @Email String email,

        @NotBlank String username,
        @NotBlank String password,
        @Valid AuthCreateRoleRequest roleRequest
) {}
