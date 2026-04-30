package cj.projects.taskmanager.persistence.repositories;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static cj.projects.taskmanager.DataProvider.TaskDataProvider.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer=
            new PostgreSQLContainer("postgres:latest");
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

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
    void findAllTaskTest(){

        Pageable pageable=PageRequest.of(0,3);
        Page<TaskEntity> taskPage= taskRepository.findAll(pageable);

        assertThat(taskPage.getTotalElements()).isEqualTo(3);
        assertThat(taskPage.getContent().getFirst().getTitle()).isEqualTo(task1.getTitle());
        assertThat(taskPage.getContent().get(1).getTitle()).isEqualTo(task2.getTitle());
        assertThat(taskPage.getContent().get(2).getTitle()).isEqualTo(task3.getTitle());

    }

    @Test
    void findTaskEntitiesById() {
    }

    @Test
    void findTaskEntitiesByStatus() {
    }

    @Test
    void findTaskEntitiesByAuthor() {
    }

    @Test
    void findTaskEntitiesByCreateAdBetween() {
    }

    @Test
    void saveTaskTest(){
    }

    @Test
    void updateTaskTestById(){
    }

    @Test
    void deleteTaskTestById(){
    }



}