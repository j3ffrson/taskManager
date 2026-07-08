package cj.projects.taskmanager.controller;

import cj.projects.taskmanager.services.TaskService;
import cj.projects.taskmanager.services.dto.request.TaskRequest;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/tasks",version = "1.0")
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    @PreAuthorize("hasAuthority('READ')")
    ResponseEntity<Page<TaskDto>> getAllTask(@RequestParam(name = "page",defaultValue = "0") int pageNumber,
                                             @RequestParam(name = "size",defaultValue = "3") int pageSize){

        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        return new ResponseEntity<>(taskService.findAllTaskPage(pageable), HttpStatus.OK);

    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('READ')")
    ResponseEntity<Page<TaskDto>> getAllTaskByStatus(@RequestParam(name = "page",defaultValue = "0") int pageNumber,
                                                     @RequestParam(name = "size",defaultValue = "3") int pageSize,
                                                     @PathVariable String status){

        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        return new ResponseEntity<>(taskService.findAllTaskByStatusPage(status,pageable), HttpStatus.OK);

    }

    @GetMapping("/author")
    @PreAuthorize("hasAuthority('READ')")
    ResponseEntity<Page<TaskDto>> getAllTaskByAuthor(@RequestParam(name = "page",defaultValue = "0") int pageNumber,
                                                     @RequestParam(name = "size",defaultValue = "3") int pageSize){

        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        return new ResponseEntity<>(taskService.findAllTaskByAuthorPage(pageable), HttpStatus.OK);

    }

    @GetMapping("/period/date")
    @PreAuthorize("hasAuthority('READ')")
    ResponseEntity<Page<TaskDto>> getAllTaskByPeriodTime(@RequestParam(name = "page",defaultValue = "0") int pageNumber, @RequestParam(name = "size",defaultValue = "3") int pageSize,
                                                         @RequestParam(name = "after") String createAdDateAfter, @RequestParam(name = "before") String createAdDateBefore){

        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateStart= LocalDate.parse(createAdDateAfter,formatDate);
        LocalDate dateEnd= LocalDate.parse(createAdDateBefore,formatDate);

        return new ResponseEntity<>(taskService.findAllTaskByCreateAdBetween(dateStart,dateEnd,pageable), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ')")
    ResponseEntity<TaskDto> getTaskById(@PathVariable String id){
        return new ResponseEntity<>(taskService.findTaskById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('CREATE')")
    ResponseEntity<TaskDto> createNewTask(@RequestBody @Valid TaskRequest taskRequest){
        return new ResponseEntity<>(taskService.createNewTask(taskRequest),HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    ResponseEntity<TaskDto> updateTask(@RequestBody @Valid TaskRequest taskRequest, @PathVariable String id) {
        return new ResponseEntity<>(taskService.updateNewTask(taskRequest,UUID.fromString(id)),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    ResponseEntity<Void> deleteTask(@PathVariable String id){
        taskService.deleteTaskById(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
