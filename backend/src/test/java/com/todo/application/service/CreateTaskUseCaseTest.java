package com.todo.application.service;

import com.todo.application.dto.CreateTaskRequest;
import com.todo.application.dto.TaskResponse;
import com.todo.application.mapper.TaskApplicationMapper;
import com.todo.domain.model.*;
import com.todo.domain.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateTaskUseCase")
class CreateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskApplicationMapper mapper;

    @InjectMocks
    private CreateTaskUseCase useCase;

    @Nested
    @DisplayName("execute")
    class Execute {

        @Test
        @DisplayName("Deve criar tarefa com sucesso")
        void shouldCreateTaskSuccessfully() {
            CreateTaskRequest request = CreateTaskRequest.builder()
                    .title("Nova Tarefa")
                    .description("Descrição da tarefa")
                    .priority(CreateTaskRequest.PriorityDto.HIGH)
                    .dueDate(LocalDate.now().plusDays(7))
                    .build();

            Task savedTask = Task.create(
                    TaskTitle.of(request.getTitle()),
                    TaskDescription.of(request.getDescription()),
                    Priority.HIGH,
                    DueDate.of(request.getDueDate())
            );

            TaskResponse expectedResponse = TaskResponse.builder()
                    .id(savedTask.getId().getValue())
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .priority("HIGH")
                    .status("PENDING")
                    .dueDate(request.getDueDate())
                    .createdAt(savedTask.getCreatedAt())
                    .updatedAt(savedTask.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
            when(mapper.toResponse(savedTask)).thenReturn(expectedResponse);

            TaskResponse response = useCase.execute(request);

            assertThat(response).isNotNull();
            assertThat(response.getTitle()).isEqualTo("Nova Tarefa");
            assertThat(response.getPriority()).isEqualTo("HIGH");
            assertThat(response.getStatus()).isEqualTo("PENDING");

            verify(taskRepository).save(any(Task.class));
            verify(mapper).toResponse(savedTask);
        }

        @Test
        @DisplayName("Deve criar tarefa sem descrição")
        void shouldCreateTaskWithoutDescription() {
            CreateTaskRequest request = CreateTaskRequest.builder()
                    .title("Tarefa sem descrição")
                    .priority(CreateTaskRequest.PriorityDto.MEDIUM)
                    .build();

            Task savedTask = Task.create(
                    TaskTitle.of(request.getTitle()),
                    TaskDescription.of(null),
                    Priority.MEDIUM,
                    DueDate.of(null)
            );

            TaskResponse expectedResponse = TaskResponse.builder()
                    .id(savedTask.getId().getValue())
                    .title(request.getTitle())
                    .description(null)
                    .priority("MEDIUM")
                    .status("PENDING")
                    .dueDate(null)
                    .createdAt(savedTask.getCreatedAt())
                    .updatedAt(savedTask.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
            when(mapper.toResponse(savedTask)).thenReturn(expectedResponse);

            TaskResponse response = useCase.execute(request);

            assertThat(response.getDescription()).isNull();
        }

        @Test
        @DisplayName("Deve criar tarefa sem data limite")
        void shouldCreateTaskWithoutDueDate() {
            CreateTaskRequest request = CreateTaskRequest.builder()
                    .title("Tarefa sem data")
                    .priority(CreateTaskRequest.PriorityDto.LOW)
                    .dueDate(null)
                    .build();

            Task savedTask = Task.create(
                    TaskTitle.of(request.getTitle()),
                    TaskDescription.of(null),
                    Priority.LOW,
                    DueDate.of(null)
            );

            TaskResponse expectedResponse = TaskResponse.builder()
                    .id(savedTask.getId().getValue())
                    .title(request.getTitle())
                    .description(null)
                    .priority("LOW")
                    .status("PENDING")
                    .dueDate(null)
                    .createdAt(savedTask.getCreatedAt())
                    .updatedAt(savedTask.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
            when(mapper.toResponse(savedTask)).thenReturn(expectedResponse);

            TaskResponse response = useCase.execute(request);

            assertThat(response.getDueDate()).isNull();
        }
    }
}