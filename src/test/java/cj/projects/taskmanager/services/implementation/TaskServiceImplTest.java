package cj.projects.taskmanager.services.implementation;

import cj.projects.taskmanager.persistence.entities.TaskEntity;
import cj.projects.taskmanager.persistence.entities.UserEntity;
import cj.projects.taskmanager.persistence.entities.enums.Status;
import cj.projects.taskmanager.persistence.repositories.TaskRepository;
import cj.projects.taskmanager.persistence.repositories.UserRepository;
import cj.projects.taskmanager.services.dto.request.TaskRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static cj.projects.taskmanager.DataProvider.TaskDataProvider.*;
import static cj.projects.taskmanager.DataProvider.UserDataProvider.getUser;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // Given
        Pageable pageable = PageRequest.of(0, 5);
        UserEntity author = getUser();
        List<TaskEntity> taskList = List.of(getTaskEntity1(), getTaskEntity2());
        List<TaskDto> taskDtoList = List.of(getTaskDto1(), getTaskDto2());
        Page<TaskEntity> entityPage = new PageImpl<>(taskList, pageable, 2);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(author.getUsername());
        when(userRepository.findByUsername(author.getUsername())).thenReturn(Optional.of(author));
        when(taskRepository.findTaskEntitiesByAuthor(author, pageable)).thenReturn(entityPage);

        // When
        Page<TaskDto> dtoPage = taskService.findAllTaskByAuthorPage(pageable);

        // Then
        assertThat(dtoPage.getContent()).isEqualTo(taskDtoList);
        assertThat(dtoPage.getContent().size()).isEqualTo(2);
        assertThat(dtoPage.getContent().getFirst().getAuthor().getUsername()).isEqualTo(author.getUsername());
        assertThat(dtoPage.getContent().get(1).getAuthor().getUsername()).isEqualTo(author.getUsername());
    }

    @Test
    void findAllTaskByCreateAdDateBetweenTest() {
        // Given
        Pageable pageable = PageRequest.of(0, 5);
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        List<TaskEntity> taskList = List.of(getTaskEntity1(), getTaskEntity2(), getTaskEntity3());
        List<TaskDto> taskDtoList = List.of(getTaskDto1(), getTaskDto2(), getTaskDto3());
        Page<TaskEntity> entityPage = new PageImpl<>(taskList, pageable, 3);

        when(taskRepository.findTaskEntitiesByCreateAdBetween(startDate, endDate, pageable)).thenReturn(entityPage);

        // When
        Page<TaskDto> dtoPage = taskService.findAllTaskByCreateAdBetween(startDate, endDate, pageable);

        // Then
        assertThat(dtoPage.getContent()).isEqualTo(taskDtoList);
        assertThat(dtoPage.getContent().size()).isEqualTo(3);
        assertThat(dtoPage.getContent().getFirst().getCreateAd()).isEqualTo(taskDtoList.getFirst().getCreateAd());
    }

    @Test
    void findTaskByIdTest() {
        // Given
        TaskEntity taskEntity = getTaskEntity1();
        TaskDto taskDto = getTaskDto1();
        UUID taskId = taskEntity.getId();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        // When
        TaskDto foundTaskDto = taskService.findTaskById(taskId);

        // Then
        assertThat(foundTaskDto).isEqualTo(taskDto);
        assertThat(foundTaskDto.getId()).isEqualTo(taskDto.getId());
        assertThat(foundTaskDto.getTitle()).isEqualTo(taskDto.getTitle());
    }

    @Test
    void createNewTaskTest() {
        // Given
        TaskRequest taskRequest = getTaskRequest();
        UserEntity author = getUser();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(author.getUsername());
        when(userRepository.findByUsername(author.getUsername())).thenReturn(Optional.of(author));

        TaskEntity savedTaskEntity = TaskEntity.builder()
                .id(UUID.randomUUID())
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(Status.NEW)
                .createAd(LocalDate.now())
                .createAdTime(LocalTime.now())
                .author(author)
                .build();

        when(taskRepository.save(any(TaskEntity.class))).thenReturn(savedTaskEntity);

        // When
        TaskDto createdTaskDto = taskService.createNewTask(taskRequest);

        // Then
        assertThat(createdTaskDto).isNotNull();
        assertThat(createdTaskDto.getTitle()).isEqualTo(taskRequest.title());
        assertThat(createdTaskDto.getDescription()).isEqualTo(taskRequest.description());
        assertThat(createdTaskDto.getStatus()).isEqualTo(Status.NEW.name());
        assertThat(createdTaskDto.getAuthor().getUsername()).isEqualTo(author.getUsername());
    }

    @Test
    void updateNewTaskTest() {
    }

    @Test
    void deleteTaskByIdTest() {
    }
}