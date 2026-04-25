package cj.projects.taskmanager.services.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TaskDateBetweenFilterRequest(
        @NotBlank(message = "Debe haber una fecha de inicio")
        String createAdDateAfter,
        @NotBlank(message = "Debe haber una fecha de fin")
        String createAdDateBefore
) {
}
