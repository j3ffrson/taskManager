package cj.projects.taskmanager.Exceptions;

import cj.projects.taskmanager.services.dto.exceptions.BadRequestDto;
import cj.projects.taskmanager.services.dto.exceptions.ErrorExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req){

        List<ErrorExceptionDto>errors= new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(err->{
            String fieldName= ((FieldError)err).getField();
            String errorMessage=err.getDefaultMessage();
            errors.add(new ErrorExceptionDto(400,fieldName,errorMessage));
        });
        BadRequestDto bad= new BadRequestDto(errors, req.getDescription(false));
        return ResponseEntity.badRequest().body(bad);

    }

}
