package com.todo.application.service;

import com.todo.application.dto.TaskResponse;
import com.todo.application.dto.UpdateTaskRequest;
import com.todo.application.mapper.TaskApplicationMapper;
import com.todo.domain.model.Task;
import com.todo.domain.model.TaskId;
import com.todo.domain.model.TaskStatus;
import com.todo.domain.repository.TaskRepository;
import com.todo.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateTaskUseCase")
class UpdateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskApplicationMapper mapper;

    @InjectMocks
    private UpdateTaskUseCase useCase;

    @Nested
    @DisplayName("execute")
    class Execute {

        @Test
        @DisplayName("Deve atualizar todos os campos da tarefa")
        void shouldUpdateAllFields() {
            String id = UUID.randomUUID().toString();
            Task existingTask = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Título Original"),
                    TaskDescription.of("Descrição Original"),
                    Priority.LOW,
                    TaskStatus.PENDING,
                    DueDate.of(LocalDate.now().plusDays(1)),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().minusHours(1)
            );

            UpdateTaskRequest request = UpdateTaskRequest.builder()
                    .title("Novo Título")
                    .description("Nova Descrição")
                    .priority(UpdateTaskRequest.PriorityDto.HIGH)
                    .dueDate(LocalDate.now().plusDays(10))
                    .build();

            Task updatedTask = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Novo Título"),
                    TaskDescription.of("Nova Descrição"),
                    Priority.HIGH,
                    TaskStatus.PENDING,
                    DueDate.of(LocalDate.now().plusDays(10)),
                    existingTask.getCreatedAt(),
                    LocalDateTime.now()
            );

            TaskResponse expectedResponse = TaskResponse.builder()
                    .id(UUID.fromString(id))
                    .title("Novo Título")
                    .description("Nova Descrição")
                    .priority("HIGH")
                    .status("PENDING")
                    .dueDate(LocalDate.now().plusDays(10))
                    .createdAt(existingTask.getCreatedAt())
                    .updatedAt(updatedTask.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findById(TaskId.of(id))).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
            when(mapper.toResponse(updatedTask)).thenReturn(expectedResponse);

            TaskResponse response = useCase.execute(id, request);

            assertThat(response.getTitle()).isEqualTo("Novo Título");
            assertThat(response.getDescription()).isEqualTo("Nova Descrição");
            assertThat(response.getPriority()).isEqualTo("HIGH");
            assertThat(response.getDueDate()).isEqualTo(LocalDate.now().plusDays(10));
        }

        @Test
        @DisplayName("Deve atualizar apenas título")
        void shouldUpdateOnlyTitle() {
            String id = UUID.randomUUID().toString();
            Task existingTask = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Original"),
                    TaskDescription.of("Desc"),
                    Priority.MEDIUM,
                    TaskStatus.PENDING,
                    DueDate.of(LocalDate.now().plusDays(5)),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().minusHours(1)
            );

            UpdateTaskRequest request = UpdateTaskRequest.builder()
                    .title("Apenas Título")
                    .build();

            Task updatedTask = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Apenas Título"),
                    TaskDescription.of("Desc"),
                    Priority.MEDIUM,
                    TaskStatus.PENDING,
                    DueDate.of(LocalDate.now().plusDays(5)),
                    existingTask.getCreatedAt(),
                    LocalDateTime.now()
            );

            TaskResponse expectedResponse = TaskResponse.builder()
                    .id(UUID.fromString(id))
                    .title("Apenas Título")
                    .description("Desc")
                    .priority("MEDIUM")
                    .status("PENDING")
                    .dueDate(LocalDate.now().plusDays(5))
                    .createdAt(existingTask.getCreatedAt())
                    .updatedAt(updatedTask.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findById(TaskId.of(id))).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
            when(mapper.toResponse(updatedTask)).thenReturn(expectedResponse);

            TaskResponse response = useCase.execute(id, request);

            assertThat(response.getTitle()).isEqualTo("Apenas Título");
            assertThat(response.getDescription()).isEqualTo("Desc");
            assertThat(response.getPriority()).isEqualTo("MEDIUM");
        }

        @Test
        @DisplayName("Deve lançar exceção quando tarefa não encontrada")
        void shouldThrowWhenTaskNotFound() {
            String id = UUID.randomUUID().toString();
            UpdateTaskRequest request = UpdateTaskRequest.builder().title("Novo").build();

            when(taskRepository.findById(TaskId.of(id))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(id, request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}