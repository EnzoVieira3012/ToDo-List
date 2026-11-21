package com.todo.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.presentation.dto.CreateTaskRequest;
import com.todo.presentation.dto.UpdateTaskRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DisplayName("TaskController Integration Tests")
class TaskControllerIntegrationTest {

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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve criar tarefa via POST /api/tasks")
    void shouldCreateTask() throws Exception {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Nova Tarefa via API")
                .description("Descrição da tarefa")
                .priority(CreateTaskRequest.PriorityDto.HIGH)
                .dueDate(LocalDate.now().plusDays(7))
                .build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nova Tarefa via API"))
                .andExpect(jsonPath("$.description").value("Descrição da tarefa"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 para título vazio")
    void shouldReturn400ForEmptyTitle() throws Exception {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("")
                .priority(CreateTaskRequest.PriorityDto.MEDIUM)
                .build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    @DisplayName("Deve listar tarefas via GET /api/tasks")
    void shouldListTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    @DisplayName("Deve filtrar por status")
    void shouldFilterByStatus() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].status").value(everyItem(is("PENDING"))));
    }

    @Test
    @DisplayName("Deve filtrar por prioridade")
    void shouldFilterByPriority() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].priority").value(everyItem(is("HIGH"))));
    }

    @Test
    @DisplayName("Deve buscar tarefa por ID")
    void shouldGetTaskById() throws Exception {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Tarefa para Buscar")
                .priority(CreateTaskRequest.PriorityDto.MEDIUM)
                .build();

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Tarefa para Buscar"));
    }

    @Test
    @DisplayName("Deve retornar 404 para ID inexistente")
    void shouldReturn404ForNonExistentId() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", "550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("Deve atualizar tarefa via PUT")
    void shouldUpdateTask() throws Exception {
        CreateTaskRequest createRequest = CreateTaskRequest.builder()
                .title("Original")
                .priority(CreateTaskRequest.PriorityDto.LOW)
                .build();

        String createResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        UpdateTaskRequest updateRequest = UpdateTaskRequest.builder()
                .title("Atualizado")
                .priority(UpdateTaskRequest.PriorityDto.HIGH)
                .build();

        mockMvc.perform(put("/api/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Atualizado"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    @DisplayName("Deve alternar status via PATCH /toggle")
    void shouldToggleStatus() throws Exception {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Para Toggle")
                .priority(CreateTaskRequest.PriorityDto.MEDIUM)
                .build();

        String createResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        mockMvc.perform(patch("/api/tasks/{id}/toggle", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        mockMvc.perform(patch("/api/tasks/{id}/toggle", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Deve deletar tarefa via DELETE")
    void shouldDeleteTask() throws Exception {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Para Deletar")
                .priority(CreateTaskRequest.PriorityDto.MEDIUM)
                .build();

        String createResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        mockMvc.perform(delete("/api/tasks/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve paginar resultados")
    void shouldPaginateResults() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.page").value(0));
    }
}