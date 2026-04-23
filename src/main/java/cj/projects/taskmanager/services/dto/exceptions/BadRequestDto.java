package cj.projects.taskmanager.services.dto.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class BadRequestDto {

    private Date timestamp= new Date();
    private List<ErrorExceptionDto> errors;
    private String path;

    public BadRequestDto(List<ErrorExceptionDto>errors,String path){

        this.errors = errors;
        this.path = path;

    }

}
