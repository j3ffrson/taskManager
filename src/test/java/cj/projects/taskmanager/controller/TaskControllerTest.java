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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity author;
    private TaskEntity task1;
    private TaskEntity task2;
    private TaskEntity task3;
    @BeforeEach
    void setUp() {

        taskRepository.deleteAll();

        author= getUserEntity();
        author.setPassword(passwordEncoder.encode("passtest"));

        userRepository.save(author);

        task1= getTaskEntity1NonId();
        task2= getTaskEntity2NonId();
        task3= getTaskEntity3NonId();

        task1.setAuthor(author);
        task2.setAuthor(author);
        task3.setAuthor(author);

        task1.setCreateAd(LocalDate.of(2026,3,30));
        task2.setCreateAd(LocalDate.of(2026,4,1));
        task3.setCreateAd(LocalDate.of(2026,4,2));

        taskRepository.saveAll(List.of(task1,task2,task3));

    }

    @Test
    void getAllTaskTest() {

        restTestClient.get().uri("/api/tasks").header("Api-Version", "1")
                .exchange()
                .expectAll(
                        spect -> spect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spect -> spect.expectStatus().isOk(),
                        spect -> spect.expectBody(Map.class),
                        spect -> spect.expectBody().jsonPath("$.content").isArray(),
                        spect -> spect.expectBody().jsonPath("$.page.number").isEqualTo(0),
                        spect -> spect.expectBody().jsonPath("$.page.totalElements").isEqualTo(3),
                        spect -> spect.expectBody().jsonPath("$.page.size").isEqualTo(3)
                );
    }

    @Test
    void getAllTaskByStatusTest() {

        restTestClient.get().uri("/api/tasks/status/NEW").header("Api-Version", "1")
                .exchange()
                .expectAll(
                        spect -> spect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spect -> spect.expectStatus().isOk(),
                        spect -> spect.expectBody(Map.class),
                        spect -> spect.expectBody().jsonPath("$.content").isArray(),
                        spect -> spect.expectBody().jsonPath("$.page.number").isEqualTo(0),
                        spect -> spect.expectBody().jsonPath("$.page.totalElements").isEqualTo(2),
                        spect -> spect.expectBody().jsonPath("$.page.size").isEqualTo(3)
                );

    }

    @Test
    void getAllTaskByAuthorTest() {
        restTestClient.get().uri("/api/tasks/author").header("Api-Version", "1")
                .headers(headers -> headers.setBasicAuth("jeffer", "passtest"))
                .exchange()
                .expectAll(
                        spect -> spect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spect -> spect.expectStatus().isOk(),
                        spect -> spect.expectBody(Map.class),
                        spect -> spect.expectBody().jsonPath("$.content").isArray(),
                        spect -> spect.expectBody().jsonPath("$.content[0].author.username").isEqualTo(author.getUsername()),
                        spect -> spect.expectBody().jsonPath("$.content[0].author.name").isEqualTo(author.getName()),
                        spect -> spect.expectBody().jsonPath("$.page.totalElements").isEqualTo(3)
                );
    }

    @Test
    void getAllTaskByPeriodTimeTest() {

        restTestClient.post().uri("/api/tasks/period/date").header("Api-Version","1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TaskDateBetweenFilterRequest("2026-04-01","2026-04-03"))
                .exchange()
                .expectAll(
                        spect -> spect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spect -> spect.expectStatus().isOk(),
                        spect -> spect.expectBody(Map.class),
                        spect -> spect.expectBody().jsonPath("$.content").isArray(),
                        spect -> spect.expectBody().jsonPath("$.page.totalElements").isEqualTo(2),
                        spect -> spect.expectBody().jsonPath("$.content[0].createAd").isEqualTo("01/04/2026"),
                        spect -> spect.expectBody().jsonPath("$.content[1].createAd").isEqualTo("02/04/2026")
                );

    }

    @Test
    void getTaskByIdTest() {

        restTestClient.get().uri("/api/tasks/"+task1.getId().toString()).header("Api-Version","1")
                .exchange()
                .expectAll(
                        expect-> expect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        expect -> expect.expectStatus().isOk(),
                        expect -> expect.expectBody(TaskDto.class),
                        expect -> expect.expectBody().jsonPath("$.id").isEqualTo(task1.getId().toString())
                );

    }

    @Test
    void createNewTaskTest() {

        TaskRequest taskRequest = getTaskRequest();
        restTestClient.post().uri("/api/tasks/new").header("Api-Version","1")
                .headers(header->header.setBasicAuth("jeffer","passtest"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskRequest)
                .exchange()
                .expectAll(
                        expect -> expect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        expect -> expect.expectStatus().isCreated(),
                        expect -> expect.expectBody(TaskDto.class),
                        expect -> expect.expectBody().jsonPath("$.id").isNotEmpty(),
                        expect -> expect.expectBody().jsonPath("$.title").isEqualTo(taskRequest.title())
                );

    }

    @Test
    void updateTaskTest() {

        TaskRequest taskRequest = new TaskRequest(null,null,"FINISHED");
        restTestClient.put().uri("/api/tasks/update/"+task1.getId().toString()).header("Api-Version","1")
                .headers(header->header.setBasicAuth("jeffer","passtest"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskRequest)
                .exchange()
                .expectAll(
                        expect -> expect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        expect -> expect.expectStatus().isOk(),
                        expect -> expect.expectBody(TaskDto.class),
                        expect -> expect.expectBody().jsonPath("$.id").isEqualTo(task1.getId().toString()),
                        expect -> expect.expectBody().jsonPath("$.title").isEqualTo(task1.getTitle()),
                        expect -> expect.expectBody().jsonPath("$.status").isEqualTo(Status.FINISHED.name()),
                        expect -> expect.expectBody().jsonPath("$.updateAd").isNotEmpty()
                );

    }

    @Test
    void deleteTaskTest() {

        restTestClient.delete().uri("/api/tasks/delete/"+task1.getId().toString()).header("Api-Version","1")
                .exchange()
                .expectAll(
                        expect -> expect.expectBody().isEmpty(),
                        expect -> expect.expectStatus().isNoContent()

                );

    }
}