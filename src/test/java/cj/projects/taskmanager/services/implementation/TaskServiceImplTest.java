package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import cj.projects.taskmanager.persistence.repositories.TaskRepository;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.dto.response.TaskDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static cj.projects.taskmanager.DataProvider.TaskDataProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

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

        Pageable pageable= PageRequest.of(1,3);
        List<TaskEntity> taskList= List.of(getTaskEntity1(),getTaskEntity2(),getTaskEntity3());
        List<TaskDto> taskDtoList= List.of(getTaskDto1(),getTaskDto2(),getTaskDto3());
        Page<TaskEntity> entityPage= new PageImpl<>(taskList,pageable,3);

        when(taskRepository.findAll(pageable)).thenReturn(entityPage);

        Page<TaskDto> dtoPage= taskService.findAllTaskPage(pageable);

        assertThat(dtoPage.getContent()).isEqualTo(taskDtoList);
        assertThat(dtoPage.getContent().size()).isEqualTo(taskDtoList.size());
        assertThat(dtoPage.getContent().getFirst().getTitle()).isEqualTo(taskDtoList.getFirst().getTitle());
        assertThat(dtoPage.getContent().get(1).getTitle()).isEqualTo(taskDtoList.get(1).getTitle());
        assertThat(dtoPage.getContent().getLast().getTitle()).isEqualTo(taskDtoList.getLast().getTitle());
        assertThat(dtoPage.getContent().getFirst().getId()).isEqualTo(taskDtoList.getFirst().getId());

    }

    @Test
    void findAllTaskByStatusPageTest() {

        Pageable pageable= PageRequest.of(1,3);
        List<TaskEntity> taskList= List.of(getTaskEntity1(),getTaskEntity2());
        List<TaskDto> taskDtoList= List.of(getTaskDto1(),getTaskDto2());
        Page<TaskEntity> entityPage= new PageImpl<>(taskList,pageable,3);

        when(taskRepository.findTaskEntitiesByStatus(Status.NEW,pageable)).thenReturn(entityPage);

        Page<TaskDto> dtoPage= taskService.findAllTaskByStatusPage("NEW",pageable);

        assertThat(dtoPage.getContent()).isEqualTo(taskDtoList);
        assertThat(dtoPage.getContent().size()).isEqualTo(2);
        assertThat(dtoPage.getContent().getFirst().getStatus()).isEqualTo(taskDtoList.getFirst().getStatus());

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