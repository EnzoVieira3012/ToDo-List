package com.todo.application.service;

import com.todo.domain.model.TaskId;
import com.todo.domain.repository.TaskRepository;
import com.todo.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteTaskUseCase")
class DeleteTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DeleteTaskUseCase useCase;

    @Nested
    @DisplayName("execute")
    class Execute {

        @Test
        @DisplayName("Deve deletar tarefa existente")
        void shouldDeleteExistingTask() {
            String id = UUID.randomUUID().toString();

            when(taskRepository.existsById(TaskId.of(id))).thenReturn(true);

            useCase.execute(id);

            verify(taskRepository).existsById(TaskId.of(id));
            verify(taskRepository).deleteById(TaskId.of(id));
        }

        @Test
        @DisplayName("Deve lançar exceção quando tarefa não encontrada")
        void shouldThrowWhenTaskNotFound() {
            String id = UUID.randomUUID().toString();

            when(taskRepository.existsById(TaskId.of(id))).thenReturn(false);

            assertThatThrownBy(() -> useCase.execute(id))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Task not found with id: " + id);

            verify(taskRepository).existsById(TaskId.of(id));
            verify(taskRepository, never()).deleteById(any());
        }
    }
}