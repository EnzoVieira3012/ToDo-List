package com.todo.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class ValueObjectsTest {

    @Nested
    @DisplayName("TaskId")
    class TaskIdTest {

        @Test
        @DisplayName("Deve gerar ID aleatório")
        void shouldGenerateRandomId() {
            TaskId id = TaskId.generate();

            assertThat(id).isNotNull();
            assertThat(id.getValue()).isNotNull();
        }

        @Test
        @DisplayName("Deve criar ID a partir de UUID")
        void shouldCreateFromUuid() {
            UUID uuid = UUID.randomUUID();
            TaskId id = TaskId.of(uuid);

            assertThat(id.getValue()).isEqualTo(uuid);
        }

        @Test
        @DisplayName("Deve criar ID a partir de String")
        void shouldCreateFromString() {
            String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
            TaskId id = TaskId.of(uuidStr);

            assertThat(id.getValue().toString()).isEqualTo(uuidStr);
        }

        @Test
        @DisplayName("Deve lançar exceção para UUID nulo")
        void shouldThrowForNullUuid() {
            assertThatThrownBy(() -> TaskId.of((UUID) null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("TaskId cannot be null");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "invalid-uuid"})
        @DisplayName("Deve lançar exceção para String inválida")
        void shouldThrowForInvalidString(String invalid) {
            assertThatThrownBy(() -> TaskId.of(invalid))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Dois IDs com mesmo UUID devem ser iguais")
        void shouldBeEqualForSameUuid() {
            UUID uuid = UUID.randomUUID();
            TaskId id1 = TaskId.of(uuid);
            TaskId id2 = TaskId.of(uuid);

            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }
    }

    @Nested
    @DisplayName("TaskTitle")
    class TaskTitleTest {

        @Test
        @DisplayName("Deve criar título válido")
        void shouldCreateValidTitle() {
            TaskTitle title = TaskTitle.of("  Minha Tarefa  ");

            assertThat(title.getValue()).isEqualTo("Minha Tarefa");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        @DisplayName("Deve lançar exceção para título inválido")
        void shouldThrowForInvalidTitle(String invalid) {
            assertThatThrownBy(() -> TaskTitle.of(invalid))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cannot be");
        }

        @Test
        @DisplayName("Deve lançar exceção para título muito longo")
        void shouldThrowForTooLongTitle() {
            String longTitle = "a".repeat(201);
            assertThatThrownBy(() -> TaskTitle.of(longTitle))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("200");
        }

        @Test
        @DisplayName("Deve aceitar título com 200 caracteres")
        void shouldAccept200CharTitle() {
            String title200 = "a".repeat(200);
            TaskTitle title = TaskTitle.of(title200);

            assertThat(title.getValue()).hasSize(200);
        }
    }

    @Nested
    @DisplayName("TaskDescription")
    class TaskDescriptionTest {

        @Test
        @DisplayName("Deve criar descrição válida")
        void shouldCreateValidDescription() {
            TaskDescription desc = TaskDescription.of("  Minha descrição  ");

            assertThat(desc.getValue()).isEqualTo("Minha descrição");
            assertThat(desc.isPresent()).isTrue();
        }

        @Test
        @DisplayName("Deve aceitar null e retornar descrição vazia")
        void shouldAcceptNull() {
            TaskDescription desc = TaskDescription.of(null);

            assertThat(desc.getValue()).isNull();
            assertThat(desc.isPresent()).isFalse();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("Deve tratar strings vazias como null")
        void shouldTreatEmptyAsNull(String empty) {
            TaskDescription desc = TaskDescription.of(empty);

            assertThat(desc.getValue()).isNull();
            assertThat(desc.isPresent()).isFalse();
        }

        @Test
        @DisplayName("Deve lançar exceção para descrição muito longa")
        void shouldThrowForTooLongDescription() {
            String longDesc = "a".repeat(1001);
            assertThatThrownBy(() -> TaskDescription.of(longDesc))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("1000");
        }
    }

    @Nested
    @DisplayName("DueDate")
    class DueDateTest {

        @Test
        @DisplayName("Deve criar data válida")
        void shouldCreateValidDate() {
            LocalDate date = LocalDate.now().plusDays(5);
            DueDate dueDate = DueDate.of(date);

            assertThat(dueDate.getValue()).isEqualTo(date);
            assertThat(dueDate.isPresent()).isTrue();
        }

        @Test
        @DisplayName("Deve aceitar null")
        void shouldAcceptNull() {
            DueDate dueDate = DueDate.of((LocalDate) null);

            assertThat(dueDate.getValue()).isNull();
            assertThat(dueDate.isPresent()).isFalse();
        }

        @Test
        @DisplayName("Deve criar a partir de String ISO")
        void shouldCreateFromIsoString() {
            DueDate dueDate = DueDate.of("2025-12-31");

            assertThat(dueDate.getValue()).isEqualTo(LocalDate.of(2025, 12, 31));
        }

        @Test
        @DisplayName("Deve detectar data passada")
        void shouldDetectPastDate() {
            DueDate dueDate = DueDate.of(LocalDate.now().minusDays(1));

            assertThat(dueDate.isOverdue()).isTrue();
            assertThat(dueDate.isFuture()).isFalse();
        }

        @Test
        @DisplayName("Deve detectar data futura")
        void shouldDetectFutureDate() {
            DueDate dueDate = DueDate.of(LocalDate.now().plusDays(1));

            assertThat(dueDate.isFuture()).isTrue();
            assertThat(dueDate.isOverdue()).isFalse();
        }

        @Test
        @DisplayName("Deve detectar data de hoje")
        void shouldDetectToday() {
            DueDate dueDate = DueDate.of(LocalDate.now());

            assertThat(dueDate.isToday()).isTrue();
        }
    }

    @Nested
    @DisplayName("Priority")
    class PriorityTest {

        @Test
        @DisplayName("Deve criar a partir de label")
        void shouldCreateFromLabel() {
            assertThat(Priority.fromLabel("Baixa")).isEqualTo(Priority.LOW);
            assertThat(Priority.fromLabel("MÉDIA")).isEqualTo(Priority.MEDIUM);
            assertThat(Priority.fromLabel("alta")).isEqualTo(Priority.HIGH);
        }

        @Test
        @DisplayName("Deve lançar exceção para label inválido")
        void shouldThrowForInvalidLabel() {
            assertThatThrownBy(() -> Priority.fromLabel("URGENTE"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Deve comparar prioridades corretamente")
        void shouldComparePriorities() {
            assertThat(Priority.HIGH.isHigherThan(Priority.MEDIUM)).isTrue();
            assertThat(Priority.MEDIUM.isHigherThan(Priority.LOW)).isTrue();
            assertThat(Priority.LOW.isLowerThan(Priority.HIGH)).isTrue();
        }

        @Test
        @DisplayName("Deve obter peso correto")
        void shouldGetCorrectWeight() {
            assertThat(Priority.LOW.getWeight()).isEqualTo(1);
            assertThat(Priority.MEDIUM.getWeight()).isEqualTo(2);
            assertThat(Priority.HIGH.getWeight()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("TaskStatus")
    class TaskStatusTest {

        @Test
        @DisplayName("Deve criar a partir de label")
        void shouldCreateFromLabel() {
            assertThat(TaskStatus.fromLabel("Pendente")).isEqualTo(TaskStatus.PENDING);
            assertThat(TaskStatus.fromLabel("CONCLUÍDA")).isEqualTo(TaskStatus.COMPLETED);
        }

        @Test
        @DisplayName("Deve alternar status corretamente")
        void shouldToggleStatus() {
            assertThat(TaskStatus.PENDING.toggle()).isEqualTo(TaskStatus.COMPLETED);
            assertThat(TaskStatus.COMPLETED.toggle()).isEqualTo(TaskStatus.PENDING);
        }

        @Test
        @DisplayName("Deve verificar status pendente")
        void shouldCheckPending() {
            assertThat(TaskStatus.PENDING.isPending()).isTrue();
            assertThat(TaskStatus.PENDING.isCompleted()).isFalse();
        }

        @Test
        @DisplayName("Deve verificar status concluído")
        void shouldCheckCompleted() {
            assertThat(TaskStatus.COMPLETED.isCompleted()).isTrue();
            assertThat(TaskStatus.COMPLETED.isPending()).isFalse();
        }
    }
}