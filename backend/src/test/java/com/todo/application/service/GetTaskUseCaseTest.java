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
@DisplayName("GetTaskUseCase")
class GetTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskApplicationMapper mapper;

    @InjectMocks
    private GetTaskUseCase useCase;

    @Nested
    @DisplayName("execute")
    class Execute {

        @Test
        @DisplayName("Deve retornar tarefa quando encontrada")
        void shouldReturnTaskWhenFound() {
            String id = UUID.randomUUID().toString();
            Task task = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Tarefa Teste"),
                    TaskDescription.of("Descrição"),
                    Priority.HIGH,
                    TaskStatus.PENDING,
                    DueDate.of(LocalDate.now().plusDays(5)),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now()
            );

            TaskResponse expectedResponse = TaskResponse.builder()
                    .id(UUID.fromString(id))
                    .title("Tarefa Teste")
                    .description("Descrição")
                    .priority("HIGH")
                    .status("PENDING")
                    .dueDate(LocalDate.now().plusDays(5))
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findById(TaskId.of(id))).thenReturn(Optional.of(task));
            when(mapper.toResponse(task)).thenReturn(expectedResponse);

            TaskResponse response = useCase.execute(id);

            assertThat(response).isNotNull();
            assertThat(response.getId().toString()).isEqualTo(id);
            assertThat(response.getTitle()).isEqualTo("Tarefa Teste");
        }

        @Test
        @DisplayName("Deve lançar exceção quando tarefa não encontrada")
        void shouldThrowWhenTaskNotFound() {
            String id = UUID.randomUUID().toString();

            when(taskRepository.findById(TaskId.of(id))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(id))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Task not found with id: " + id);

            verify(taskRepository).findById(TaskId.of(id));
            verifyNoInteractions(mapper);
        }
    }
}