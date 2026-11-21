package com.todo.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class TaskTest {

    @Nested
    @DisplayName("Criação de tarefa")
    class Create {

        @Test
        @DisplayName("Deve criar tarefa com todos os campos válidos")
        void shouldCreateTaskWithAllValidFields() {
            TaskTitle title = TaskTitle.of("Minha tarefa");
            TaskDescription description = TaskDescription.of("Descrição da tarefa");
            Priority priority = Priority.HIGH;
            DueDate dueDate = DueDate.of(LocalDate.now().plusDays(7));

            Task task = Task.create(title, description, priority, dueDate);

            assertThat(task.getId()).isNotNull();
            assertThat(task.getTitle()).isEqualTo(title);
            assertThat(task.getDescription()).isEqualTo(description);
            assertThat(task.getPriority()).isEqualTo(priority);
            assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
            assertThat(task.getDueDate()).isEqualTo(dueDate);
            assertThat(task.getCreatedAt()).isNotNull();
            assertThat(task.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Deve criar tarefa sem descrição")
        void shouldCreateTaskWithoutDescription() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));

            assertThat(task.getDescription()).isNotNull();
            assertThat(task.getDescription().isPresent()).isFalse();
        }

        @Test
        @DisplayName("Deve criar tarefa sem data limite")
        void shouldCreateTaskWithoutDueDate() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of("Desc"), Priority.LOW, DueDate.of(null));

            assertThat(task.getDueDate()).isNotNull();
            assertThat(task.getDueDate().isPresent()).isFalse();
        }
    }

    @Nested
    @DisplayName("Comportamento de status")
    class StatusBehavior {

        @Test
        @DisplayName("Deve permitir concluir tarefa pendente")
        void shouldCompletePendingTask() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));

            task.complete();

            assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
            assertThat(task.isCompleted()).isTrue();
            assertThat(task.isPending()).isFalse();
        }

        @Test
        @DisplayName("Deve lançar exceção ao concluir tarefa já concluída")
        void shouldThrowWhenCompletingAlreadyCompletedTask() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
            task.complete();

            assertThatThrownBy(task::complete)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Task is already completed");
        }

        @Test
        @DisplayName("Deve permitir reabrir tarefa concluída")
        void shouldReopenCompletedTask() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
            task.complete();

            task.reopen();

            assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
            assertThat(task.isPending()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção ao reabrir tarefa já pendente")
        void shouldThrowWhenReopeningAlreadyPendingTask() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));

            assertThatThrownBy(task::reopen)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Task is already pending");
        }

        @Test
        @DisplayName("Deve alternar status corretamente")
        void shouldToggleStatus() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));

            task.toggleStatus();
            assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);

            task.toggleStatus();
            assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
        }
    }

    @Nested
    @DisplayName("Atualização de campos")
    class FieldUpdates {

        @Test
        @DisplayName("Deve atualizar título")
        void shouldUpdateTitle() {
            Task task = Task.create(TaskTitle.of("Original"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
            LocalDateTime beforeUpdate = task.getUpdatedAt();

            task.updateTitle(TaskTitle.of("Novo título"));

            assertThat(task.getTitle().getValue()).isEqualTo("Novo título");
            assertThat(task.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Deve atualizar descrição")
        void shouldUpdateDescription() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of("Original"), Priority.MEDIUM, DueDate.of(null));
            LocalDateTime beforeUpdate = task.getUpdatedAt();

            task.updateDescription(TaskDescription.of("Nova descrição"));

            assertThat(task.getDescription().getValue()).isEqualTo("Nova descrição");
            assertThat(task.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Deve atualizar prioridade")
        void shouldUpdatePriority() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.LOW, DueDate.of(null));
            LocalDateTime beforeUpdate = task.getUpdatedAt();

            task.updatePriority(Priority.HIGH);

            assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
            assertThat(task.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Deve atualizar data limite")
        void shouldUpdateDueDate() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
            LocalDateTime beforeUpdate = task.getUpdatedAt();
            DueDate newDueDate = DueDate.of(LocalDate.now().plusDays(5));

            task.updateDueDate(newDueDate);

            assertThat(task.getDueDate()).isEqualTo(newDueDate);
            assertThat(task.getUpdatedAt()).isAfter(beforeUpdate);
        }
    }

    @Nested
    @DisplayName("Verificação de atraso")
    class OverdueCheck {

        @Test
        @DisplayName("Deve retornar true para tarefa pendente com data passada")
        void shouldReturnTrueForPendingTaskWithPastDueDate() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM,
                    DueDate.of(LocalDate.now().minusDays(1)));

            assertThat(task.isOverdue()).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false para tarefa concluída com data passada")
        void shouldReturnFalseForCompletedTaskWithPastDueDate() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM,
                    DueDate.of(LocalDate.now().minusDays(1)));
            task.complete();

            assertThat(task.isOverdue()).isFalse();
        }

        @Test
        @DisplayName("Deve retornar false para tarefa sem data limite")
        void shouldReturnFalseForTaskWithoutDueDate() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));

            assertThat(task.isOverdue()).isFalse();
        }

        @Test
        @DisplayName("Deve retornar false para tarefa com data futura")
        void shouldReturnFalseForTaskWithFutureDueDate() {
            Task task = Task.create(TaskTitle.of("Tarefa"), TaskDescription.of(null), Priority.MEDIUM,
                    DueDate.of(LocalDate.now().plusDays(1)));

            assertThat(task.isOverdue()).isFalse();
        }
    }

    @Nested
    @DisplayName("Reconstrução a partir de persistência")
    class Reconstruct {

        @Test
        @DisplayName("Deve reconstruir tarefa com todos os campos")
        void shouldReconstructTaskWithAllFields() {
            UUID id = UUID.randomUUID();
            LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
            LocalDateTime updatedAt = LocalDateTime.now();

            Task task = Task.reconstruct(
                    TaskId.of(id),
                    TaskTitle.of("Tarefa"),
                    TaskDescription.of("Descrição"),
                    Priority.HIGH,
                    TaskStatus.COMPLETED,
                    DueDate.of(LocalDate.now().plusDays(1)),
                    createdAt,
                    updatedAt
            );

            assertThat(task.getId().getValue()).isEqualTo(id);
            assertThat(task.getCreatedAt()).isEqualTo(createdAt);
            assertThat(task.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        }
    }
}