package cj.projects.taskmanager.services.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorExceptionDto {
    private Integer code;
    private String fiel;
    private String message;
}
