package cj.projects.taskmanager.controller;

import cj.projects.taskmanager.services.TaskService;
import cj.projects.taskmanager.services.dto.request.TaskRequest;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/tasks",version = "1.0")
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    ResponseEntity<Page<TaskDto>> getAllTask(@RequestParam(name = "page",defaultValue = "0") int pageNumber,
                                             @RequestParam(name = "size",defaultValue = "3") int pageSize){

        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        return new ResponseEntity<>(taskService.findAllTaskPage(pageable), HttpStatus.OK);

    }

    @GetMapping("/status/{status}")
    ResponseEntity<Page<TaskDto>> getAllTaskByStatus(@RequestParam(name = "page",defaultValue = "0") int pageNumber,
                                                     @RequestParam(name = "size",defaultValue = "3") int pageSize,
                                                     @PathVariable String status){

        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        return new ResponseEntity<>(taskService.findAllTaskByStatusPage(status,pageable), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    ResponseEntity<TaskDto> getTaskById(@PathVariable String id){
        return new ResponseEntity<>(taskService.findTaskById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping("/new")
    ResponseEntity<TaskDto> createNewTask(@RequestBody TaskRequest taskRequest){
        return new ResponseEntity<>(taskService.createNewTask(taskRequest),HttpStatus.CREATED);
    }

}
