package cj.projects.taskmanager.controller;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.TaskRepository;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;
import java.util.Map;

import static cj.projects.taskmanager.DataProvider.TaskDataProvider.*;
import static cj.projects.taskmanager.DataProvider.TaskDataProvider.getTaskEntity3NonId;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureRestTestClient
class TaskControllerTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer=
            new PostgreSQLContainer("postgres:latest");

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    private UserEntity author;
    private TaskEntity task1;
    private TaskEntity task2;
    private TaskEntity task3;
    @BeforeEach
    void setUp() {

        taskRepository.deleteAll();

        author= getUserEntity();
        userRepository.save(author);

        task1= getTaskEntity1NonId();
        task2= getTaskEntity2NonId();
        task3= getTaskEntity3NonId();

        task1.setAuthor(author);
        task2.setAuthor(author);
        task3.setAuthor(author);

        taskRepository.saveAll(List.of(task1,task2,task3));

    }

    @Test
    void getAllTaskTest() {
    }

    @Test
    void getAllTaskByStatusTest() {
    }

    @Test
    void getAllTaskByAuthorTest() {
    }

    @Test
    void getAllTaskByPeriodTimeTest() {
    }

    @Test
    void getTaskByIdTest() {
    }

    @Test
    void createNewTaskTest() {
    }

    @Test
    void updateTaskTest() {
    }

    @Test
    void deleteTaskTest() {
    }
}