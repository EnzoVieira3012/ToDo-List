package com.todo.application.service;

import com.todo.application.dto.TaskResponse;
import com.todo.application.mapper.TaskApplicationMapper;
import com.todo.domain.model.Task;
import com.todo.domain.model.TaskId;
import com.todo.domain.repository.TaskRepository;
import com.todo.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ToggleTaskStatusUseCase {

    private final TaskRepository taskRepository;
    private final TaskApplicationMapper mapper;

    @Transactional
    public TaskResponse execute(String id) {
        TaskId taskId = TaskId.of(id);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        task.toggleStatus();
        Task saved = taskRepository.save(task);
        return mapper.toResponse(saved);
    }
}