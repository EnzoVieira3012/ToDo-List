package com.todo.infrastructure.persistence;

import com.todo.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("TaskRepository Integration Tests")
class TaskRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("todo_db")
            .withUsername("todo_user")
            .withPassword("todo_pass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private SpringDataTaskRepository springDataRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Test
    @DisplayName("Deve salvar e buscar tarefa por ID")
    void shouldSaveAndFindById() {
        Task task = Task.create(
                TaskTitle.of("Tarefa Teste"),
                TaskDescription.of("Descrição"),
                Priority.HIGH,
                DueDate.of(LocalDate.now().plusDays(7))
        );

        TaskJpaEntity entity = taskMapper.toEntity(task);
        TaskJpaEntity saved = springDataRepository.save(entity);
        Task domain = taskMapper.toDomain(saved);

        Optional<TaskJpaEntity> found = springDataRepository.findById(domain.getId().getValue());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Tarefa Teste");
        assertThat(found.get().getPriority()).isEqualTo("HIGH");
    }

    @Test
    @DisplayName("Deve buscar tarefas por status")
    void shouldFindByStatus() {
        Task pending = Task.create(TaskTitle.of("Pendente"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
        Task completed = Task.reconstruct(
                TaskId.generate(),
                TaskTitle.of("Concluída"),
                TaskDescription.of(null),
                Priority.MEDIUM,
                TaskStatus.COMPLETED,
                DueDate.of(null),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        springDataRepository.save(taskMapper.toEntity(pending));
        springDataRepository.save(taskMapper.toEntity(completed));

        List<TaskJpaEntity> pendingTasks = springDataRepository.findByStatus("PENDING");
        List<TaskJpaEntity> completedTasks = springDataRepository.findByStatus("COMPLETED");

        assertThat(pendingTasks).hasSize(1);
        assertThat(pendingTasks.get(0).getTitle()).isEqualTo("Pendente");
        assertThat(completedTasks).hasSize(1);
        assertThat(completedTasks.get(0).getTitle()).isEqualTo("Concluída");
    }

    @Test
    @DisplayName("Deve buscar tarefas por prioridade")
    void shouldFindByPriority() {
        Task high = Task.create(TaskTitle.of("Alta"), TaskDescription.of(null), Priority.HIGH, DueDate.of(null));
        Task low = Task.create(TaskTitle.of("Baixa"), TaskDescription.of(null), Priority.LOW, DueDate.of(null));

        springDataRepository.save(taskMapper.toEntity(high));
        springDataRepository.save(taskMapper.toEntity(low));

        List<TaskJpaEntity> highTasks = springDataRepository.findByPriority("HIGH");
        List<TaskJpaEntity> lowTasks = springDataRepository.findByPriority("LOW");

        assertThat(highTasks).hasSize(1);
        assertThat(highTasks.get(0).getTitle()).isEqualTo("Alta");
        assertThat(lowTasks).hasSize(1);
        assertThat(lowTasks.get(0).getTitle()).isEqualTo("Baixa");
    }

    @Test
    @DisplayName("Deve verificar existência por ID")
    void shouldCheckExistsById() {
        Task task = Task.create(TaskTitle.of("Existente"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
        TaskJpaEntity saved = springDataRepository.save(taskMapper.toEntity(task));

        boolean exists = springDataRepository.existsById(saved.getId());
        boolean notExists = springDataRepository.existsById(UUID.randomUUID());

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Deve deletar tarefa por ID")
    void shouldDeleteById() {
        Task task = Task.create(TaskTitle.of("Para Deletar"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
        TaskJpaEntity saved = springDataRepository.save(taskMapper.toEntity(task));

        springDataRepository.deleteById(saved.getId());

        Optional<TaskJpaEntity> found = springDataRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Deve contar tarefas total")
    void shouldCountTotalTasks() {
        long initialCount = springDataRepository.count();

        Task task1 = Task.create(TaskTitle.of("Tarefa 1"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
        Task task2 = Task.create(TaskTitle.of("Tarefa 2"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));

        springDataRepository.save(taskMapper.toEntity(task1));
        springDataRepository.save(taskMapper.toEntity(task2));

        long newCount = springDataRepository.count();

        assertThat(newCount).isEqualTo(initialCount + 2);
    }

    @Test
    @DisplayName("Deve contar tarefas por status")
    void shouldCountByStatus() {
        long initialPending = springDataRepository.countByStatus("PENDING");

        Task pending = Task.create(TaskTitle.of("Pendente"), TaskDescription.of(null), Priority.MEDIUM, DueDate.of(null));
        Task completed = Task.reconstruct(
                TaskId.generate(),
                TaskTitle.of("Concluída"),
                TaskDescription.of(null),
                Priority.MEDIUM,
                TaskStatus.COMPLETED,
                DueDate.of(null),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        springDataRepository.save(taskMapper.toEntity(pending));
        springDataRepository.save(taskMapper.toEntity(completed));

        long pendingCount = springDataRepository.countByStatus("PENDING");
        long completedCount = springDataRepository.countByStatus("COMPLETED");

        assertThat(pendingCount).isEqualTo(initialPending + 1);
        assertThat(completedCount).isEqualTo(1);
    }
}