package cj.projects.taskmanager.persistence.repositories;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
        userRepository.deleteAll();

        author = userRepository.save(getUserEntity());

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

        TaskEntity task= taskRepository.findById(task2.getId()).orElseThrow();

        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(task2.getId());
        assertThat(task.getTitle()).isEqualTo(task2.getTitle());
        assertThat(task.getDescription()).isEqualTo(task2.getDescription());
        assertThat(task.getStatus()).isEqualTo(task2.getStatus());

    }

    @Test
    void findTaskEntitiesByStatus() {

        Pageable pageable=PageRequest.of(0,3);
        Page<TaskEntity> taskPage= taskRepository.findTaskEntitiesByStatus(Status.NEW,pageable);

        assertThat(taskPage).isNotNull();
        assertThat(taskPage.getTotalElements()).isEqualTo(2);
        assertThat(taskPage.getTotalPages()).isEqualTo(1);
        assertThat(taskPage.getContent().getFirst().getStatus()).isEqualTo(Status.NEW);
        assertThat(taskPage.getContent().getLast().getStatus()).isEqualTo(Status.NEW);

    }

    @Test
    void findTaskEntitiesByAuthor() {

        Pageable pageable=PageRequest.of(0,3);
        Page<TaskEntity> tasksAuthor = taskRepository.findTaskEntitiesByAuthor(author,pageable);

        assertThat(tasksAuthor).isNotNull();
        assertThat(tasksAuthor.getTotalElements()).isEqualTo(3);
        assertThat(tasksAuthor.getContent()).hasSize(3);
        assertThat(tasksAuthor.getContent().getFirst().getAuthor().getId()).isEqualTo(author.getId());

    }

    @Test
    void findTaskEntitiesByCreateAdBetween() {

        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        Pageable pageable = PageRequest.of(0, 3);

        Page<TaskEntity> tasksPage = taskRepository.findTaskEntitiesByCreateAdBetween(startDate, endDate, pageable);

        assertThat(tasksPage).isNotNull();
        assertThat(tasksPage.getTotalElements()).isEqualTo(3);
        assertThat(tasksPage.getContent()).hasSize(3);

    }

    @Test
    void saveTaskTest(){

        TaskEntity task4 = getTaskEntity3NonId();
        task4.setTitle("Nueva tarea guardada");
        task4.setAuthor(author);

        TaskEntity savedTask = taskRepository.save(task4);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Nueva tarea guardada");
        assertThat(savedTask.getAuthor().getId()).isEqualTo(author.getId());

    }

    @Test
    void updateTaskTestById(){

        TaskEntity taskToUpdate = taskRepository.findById(task1.getId()).orElseThrow();
        String newTitle = "Tarea actualizada";
        taskToUpdate.setTitle(newTitle);
        taskToUpdate.setStatus(Status.FINISHED);

        TaskEntity updatedTask = taskRepository.save(taskToUpdate);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo(newTitle);
        assertThat(updatedTask.getStatus()).isEqualTo(Status.FINISHED);

    }

    @Test
    void deleteTaskTestById(){

        UUID taskId = task1.getId();
        assertThat(taskRepository.existsById(taskId)).isTrue();

        taskRepository.deleteById(taskId);

        assertThat(taskRepository.existsById(taskId)).isFalse();

    }



}