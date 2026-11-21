package com.todo.application.service;

import com.todo.application.dto.TaskResponse;
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
@DisplayName("ToggleTaskStatusUseCase")
class ToggleTaskStatusUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskApplicationMapper mapper;

    @InjectMocks
    private ToggleTaskStatusUseCase useCase;

    @Nested
    @DisplayName("execute")
    class Execute {

        @Test
        @DisplayName("Deve alternar de PENDING para COMPLETED")
        void shouldToggleFromPendingToCompleted() {
            String id = UUID.randomUUID().toString();
            Task task = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Tarefa"),
                    TaskDescription.of("Desc"),
                    Priority.MEDIUM,
                    TaskStatus.PENDING,
                    DueDate.of(LocalDate.now().plusDays(5)),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().minusHours(1)
            );

            Task toggledTask = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Tarefa"),
                    TaskDescription.of("Desc"),
                    Priority.MEDIUM,
                    TaskStatus.COMPLETED,
                    DueDate.of(LocalDate.now().plusDays(5)),
                    task.getCreatedAt(),
                    LocalDateTime.now()
            );

            TaskResponse expectedResponse = TaskResponse.builder()
                    .id(UUID.fromString(id))
                    .title("Tarefa")
                    .description("Desc")
                    .priority("MEDIUM")
                    .status("COMPLETED")
                    .dueDate(LocalDate.now().plusDays(5))
                    .createdAt(task.getCreatedAt())
                    .updatedAt(toggledTask.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findById(TaskId.of(id))).thenReturn(Optional.of(task));
            when(taskRepository.save(any(Task.class))).thenReturn(toggledTask);
            when(mapper.toResponse(toggledTask)).thenReturn(expectedResponse);

            TaskResponse response = useCase.execute(id);

            assertThat(response.getStatus()).isEqualTo("COMPLETED");
        }

        @Test
        @DisplayName("Deve alternar de COMPLETED para PENDING")
        void shouldToggleFromCompletedToPending() {
            String id = UUID.randomUUID().toString();
            Task task = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Tarefa"),
                    TaskDescription.of("Desc"),
                    Priority.MEDIUM,
                    TaskStatus.COMPLETED,
                    DueDate.of(LocalDate.now().plusDays(5)),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().minusHours(1)
            );

            Task toggledTask = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Tarefa"),
                    TaskDescription.of("Desc"),
                    Priority.MEDIUM,
                    TaskStatus.PENDING,
                    DueDate.of(LocalDate.now().plusDays(5)),
                    task.getCreatedAt(),
                    LocalDateTime.now()
            );

            TaskResponse expectedResponse = TaskResponse.builder()
                    .id(UUID.fromString(id))
                    .title("Tarefa")
                    .description("Desc")
                    .priority("MEDIUM")
                    .status("PENDING")
                    .dueDate(LocalDate.now().plusDays(5))
                    .createdAt(task.getCreatedAt())
                    .updatedAt(toggledTask.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findById(TaskId.of(id))).thenReturn(Optional.of(task));
            when(taskRepository.save(any(Task.class))).thenReturn(toggledTask);
            when(mapper.toResponse(toggledTask)).thenReturn(expectedResponse);

            TaskResponse response = useCase.execute(id);

            assertThat(response.getStatus()).isEqualTo("PENDING");
        }

        @Test
        @DisplayName("Deve lançar exceção quando tarefa não encontrada")
        void shouldThrowWhenTaskNotFound() {
            String id = UUID.randomUUID().toString();

            when(taskRepository.findById(TaskId.of(id))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(id))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}