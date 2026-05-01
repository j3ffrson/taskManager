package cj.projects.taskmanager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;


@SpringBootTest
@Testcontainers
@AutoConfigureRestTestClient
class TaskControllerTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer=
            new PostgreSQLContainer("postgres:latest");

    @Autowired
    private RestTestClient restTestClient;


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