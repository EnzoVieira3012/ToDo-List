package com.todo.application.service;

import com.todo.application.dto.PaginatedResponse;
import com.todo.application.dto.TaskFilterRequest;
import com.todo.application.dto.TaskResponse;
import com.todo.application.mapper.TaskApplicationMapper;
import com.todo.domain.model.Priority;
import com.todo.domain.model.Task;
import com.todo.domain.model.TaskId;
import com.todo.domain.model.TaskStatus;
import com.todo.domain.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListTasksUseCase")
class ListTasksUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskApplicationMapper mapper;

    @InjectMocks
    private ListTasksUseCase useCase;

    @Nested
    @DisplayName("execute")
    class Execute {

        @Test
        @DisplayName("Deve listar todas as tarefas sem filtro")
        void shouldListAllTasksWithoutFilter() {
            TaskFilterRequest filter = TaskFilterRequest.builder()
                    .page(0)
                    .size(10)
                    .sortBy("createdAt")
                    .sortDir("desc")
                    .build();

            Task task1 = Task.create(TaskTitle.of("Tarefa 1"), TaskDescription.of("Desc 1"), Priority.HIGH, DueDate.of(LocalDate.now().plusDays(5)));
            Task task2 = Task.create(TaskTitle.of("Tarefa 2"), TaskDescription.of("Desc 2"), Priority.LOW, DueDate.of(null));

            Page<Task> page = new PageImpl<>(List.of(task1, task2));

            TaskResponse response1 = TaskResponse.builder()
                    .id(task1.getId().getValue())
                    .title("Tarefa 1")
                    .description("Desc 1")
                    .priority("HIGH")
                    .status("PENDING")
                    .dueDate(LocalDate.now().plusDays(5))
                    .createdAt(task1.getCreatedAt())
                    .updatedAt(task1.getUpdatedAt())
                    .overdue(false)
                    .build();

            TaskResponse response2 = TaskResponse.builder()
                    .id(task2.getId().getValue())
                    .title("Tarefa 2")
                    .description("Desc 2")
                    .priority("LOW")
                    .status("PENDING")
                    .dueDate(null)
                    .createdAt(task2.getCreatedAt())
                    .updatedAt(task2.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findAllPaged(0, 10, "createdAt", "desc")).thenReturn(page);
            when(mapper.toResponse(task1)).thenReturn(response1);
            when(mapper.toResponse(task2)).thenReturn(response2);

            PaginatedResponse<TaskResponse> result = useCase.execute(filter);

            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getPage()).isEqualTo(0);
            assertThat(result.getSize()).isEqualTo(10);
        }

        @Test
        @DisplayName("Deve filtrar por status")
        void shouldFilterByStatus() {
            TaskFilterRequest filter = TaskFilterRequest.builder()
                    .status("COMPLETED")
                    .page(0)
                    .size(10)
                    .sortBy("createdAt")
                    .sortDir("desc")
                    .build();

            Task task = Task.reconstruct(
                    TaskId.generate(),
                    TaskTitle.of("Concluída"),
                    TaskDescription.of("Desc"),
                    Priority.MEDIUM,
                    TaskStatus.COMPLETED,
                    DueDate.of(null),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now()
            );

            Page<Task> page = new PageImpl<>(List.of(task));

            TaskResponse response = TaskResponse.builder()
                    .id(task.getId().getValue())
                    .title("Concluída")
                    .description("Desc")
                    .priority("MEDIUM")
                    .status("COMPLETED")
                    .dueDate(null)
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findByStatusPaged(TaskStatus.COMPLETED, 0, 10, "createdAt", "desc")).thenReturn(page);
            when(mapper.toResponse(task)).thenReturn(response);

            PaginatedResponse<TaskResponse> result = useCase.execute(filter);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getStatus()).isEqualTo("COMPLETED");
        }

        @Test
        @DisplayName("Deve filtrar por prioridade")
        void shouldFilterByPriority() {
            TaskFilterRequest filter = TaskFilterRequest.builder()
                    .priority("HIGH")
                    .page(0)
                    .size(10)
                    .sortBy("createdAt")
                    .sortDir("desc")
                    .build();

            Task task = Task.create(TaskTitle.of("Urgente"), TaskDescription.of(null), Priority.HIGH, DueDate.of(LocalDate.now().plusDays(1)));

            Page<Task> page = new PageImpl<>(List.of(task));

            TaskResponse response = TaskResponse.builder()
                    .id(task.getId().getValue())
                    .title("Urgente")
                    .description(null)
                    .priority("HIGH")
                    .status("PENDING")
                    .dueDate(LocalDate.now().plusDays(1))
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findByPriorityPaged(Priority.HIGH, 0, 10, "createdAt", "desc")).thenReturn(page);
            when(mapper.toResponse(task)).thenReturn(response);

            PaginatedResponse<TaskResponse> result = useCase.execute(filter);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getPriority()).isEqualTo("HIGH");
        }

        @Test
        @DisplayName("Deve filtrar por status e prioridade")
        void shouldFilterByStatusAndPriority() {
            TaskFilterRequest filter = TaskFilterRequest.builder()
                    .status("PENDING")
                    .priority("HIGH")
                    .page(0)
                    .size(10)
                    .sortBy("createdAt")
                    .sortDir("desc")
                    .build();

            Task task = Task.create(TaskTitle.of("Pendente Alta"), TaskDescription.of(null), Priority.HIGH, DueDate.of(null));

            Page<Task> page = new PageImpl<>(List.of(task));

            TaskResponse response = TaskResponse.builder()
                    .id(task.getId().getValue())
                    .title("Pendente Alta")
                    .description(null)
                    .priority("HIGH")
                    .status("PENDING")
                    .dueDate(null)
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .overdue(false)
                    .build();

            when(taskRepository.findByStatusAndPriorityPaged(TaskStatus.PENDING, Priority.HIGH, 0, 10, "createdAt", "desc")).thenReturn(page);
            when(mapper.toResponse(task)).thenReturn(response);

            PaginatedResponse<TaskResponse> result = useCase.execute(filter);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getStatus()).isEqualTo("PENDING");
            assertThat(result.getContent().get(0).getPriority()).isEqualTo("HIGH");
        }

        @Test
        @DisplayName("Deve retornar vazio quando não há tarefas")
        void shouldReturnEmptyWhenNoTasks() {
            TaskFilterRequest filter = TaskFilterRequest.builder()
                    .page(0)
                    .size(10)
                    .sortBy("createdAt")
                    .sortDir("desc")
                    .build();

            Page<Task> page = new PageImpl<>(List.of());

            when(taskRepository.findAllPaged(0, 10, "createdAt", "desc")).thenReturn(page);

            PaginatedResponse<TaskResponse> result = useCase.execute(filter);

            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();
            assertThat(result.isEmpty()).isTrue();
        }
    }
}