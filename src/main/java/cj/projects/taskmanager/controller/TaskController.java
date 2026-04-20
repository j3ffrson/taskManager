package cj.projects.taskmanager.controller;

import cj.projects.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/task",version = "1.0")
public class TaskController {

    private final TaskService taskService;

}
