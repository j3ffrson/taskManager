package cj.projects.taskmanager.controller;

import cj.projects.taskmanager.services.TaskService;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
