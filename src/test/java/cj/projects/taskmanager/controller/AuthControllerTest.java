package cj.projects.taskmanager.controller;

import cj.projects.taskmanager.DataProvider.UserDataProvider;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.dto.request.AuthCreateRoleRequest;
import cj.projects.taskmanager.services.dto.request.AuthCreateUserRequest;
import cj.projects.taskmanager.services.dto.request.AuthLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureRestTestClient
class AuthControllerTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer=
            new PostgreSQLContainer("postgres:latest");

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user= UserDataProvider.getUser();
        user.setPassword(passwordEncoder.encode("passtest"));
        user= userRepository.save(user);
    }

    @Test
    void login() {
        restTestClient.post().uri("/api/tasks/login").header("Api-Version","1")
                .contentType(MediaType.APPLICATION_JSON).body(new AuthLoginRequest("jeffer","passtest"))
                .exchange()
                .expectAll(
                        expect -> expect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        expect -> expect.expectStatus().isOk(),
                        expect -> expect.expectBody().jsonPath("$.JWT").isNotEmpty()
                );
    }

    @Test
    void sign() {
        restTestClient.post().uri("/api/tasks/sign").header("Api-Version","1").contentType(MediaType.APPLICATION_JSON)
                .body(new AuthCreateUserRequest(
                        "john","doe","chaustrejefferson@gmail.com","johnTest",
                        "passtest",new AuthCreateRoleRequest(List.of("ADMIN"))
                )).exchange()
                .expectAll(
                        expect -> expect.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        expect -> expect.expectStatus().isCreated(),
                        expect -> expect.expectBody().jsonPath("$.username").isEqualTo("johnTest"),
                        expect -> expect.expectBody().jsonPath("$.roles").isArray(),
                        expect -> expect.expectBody().jsonPath("$.roles").isEqualTo(List.of("ADMIN")),
                        expect -> expect.expectBody().jsonPath("$.JWT").isNotEmpty()
                );
    }
}