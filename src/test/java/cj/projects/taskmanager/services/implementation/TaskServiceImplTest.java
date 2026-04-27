package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.persistence.repositories.TaskRepository;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void findAllTaskPageTest() {
    }

    @Test
    void findAllTaskByStatusPageTest() {
    }

    @Test
    void findAllTaskByAuthorPageTest() {
    }

    @Test
    void findAllTaskByCreateAdDateBetweenTest() {
    }

    @Test
    void findTaskByIdTest() {
    }

    @Test
    void createNewTaskTest() {
    }

    @Test
    void updateNewTaskTest() {
    }

    @Test
    void deleteTaskByIdTest() {
    }
}