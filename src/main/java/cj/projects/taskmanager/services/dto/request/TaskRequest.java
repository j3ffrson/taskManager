package cj.projects.taskmanager.services.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(

        @Size(min = 10,max = 50, message = "El titulo debe ser de minimo 20 caracteres")
        String title,

        @Size(min = 10,max = 500,message = "La descrpcion debe ser de minimo 50 caracteres")
        String description,

        String status
) {}
