package com.todo.presentation.controller;

import com.todo.application.service.CreateTaskUseCase;
import com.todo.application.service.DeleteTaskUseCase;
import com.todo.application.service.GetTaskUseCase;
import com.todo.application.service.ListTasksUseCase;
import com.todo.application.service.ToggleTaskStatusUseCase;
import com.todo.application.service.UpdateTaskUseCase;
import com.todo.presentation.dto.CreateTaskRequest;
import com.todo.presentation.dto.PaginatedResponse;
import com.todo.presentation.dto.TaskFilterRequest;
import com.todo.presentation.dto.TaskResponse;
import com.todo.presentation.dto.UpdateTaskRequest;
import com.todo.presentation.mapper.TaskPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management API")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final ToggleTaskStatusUseCase toggleTaskStatusUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final TaskPresentationMapper presenter;

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        var appRequest = presenter.toApplicationCreate(request);
        var appResponse = createTaskUseCase.execute(appRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(presenter.toPresentationResponse(appResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TaskResponse> getById(@Parameter(description = "Task ID") @PathVariable String id) {
        var appResponse = getTaskUseCase.execute(id);
        return ResponseEntity.ok(presenter.toPresentationResponse(appResponse));
    }

    @GetMapping
    @Operation(summary = "List tasks with filters and pagination")
    public ResponseEntity<PaginatedResponse<TaskResponse>> list(
            @Parameter(description = "Filter by status: PENDING or COMPLETED") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by priority: LOW, MEDIUM, HIGH") @RequestParam(required = false) String priority,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction: asc or desc") @RequestParam(defaultValue = "desc") String sortDir) {

        var filter = TaskFilterRequest.builder()
                .status(status)
                .priority(priority)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();

        var appFilter = presenter.toApplicationFilter(filter);
        var appResponse = listTasksUseCase.execute(appFilter);
        return ResponseEntity.ok(presenter.toPresentationPage(appResponse));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task")
    public ResponseEntity<TaskResponse> update(@Parameter(description = "Task ID") @PathVariable String id,
                                               @Valid @RequestBody UpdateTaskRequest request) {
        var appRequest = presenter.toApplicationUpdate(request);
        var appResponse = updateTaskUseCase.execute(id, appRequest);
        return ResponseEntity.ok(presenter.toPresentationResponse(appResponse));
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Toggle task status (PENDING <-> COMPLETED)")
    public ResponseEntity<TaskResponse> toggleStatus(@Parameter(description = "Task ID") @PathVariable String id) {
        var appResponse = toggleTaskStatusUseCase.execute(id);
        return ResponseEntity.ok(presenter.toPresentationResponse(appResponse));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> delete(@Parameter(description = "Task ID") @PathVariable String id) {
        deleteTaskUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}