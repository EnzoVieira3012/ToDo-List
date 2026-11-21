package com.todo.application.service;

import com.todo.application.dto.TaskResponse;
import com.todo.application.dto.UpdateTaskRequest;
import com.todo.application.mapper.TaskApplicationMapper;
import com.todo.domain.model.Task;
import com.todo.domain.model.TaskDescription;
import com.todo.domain.model.TaskId;
import com.todo.domain.model.TaskTitle;
import com.todo.domain.model.Priority;
import com.todo.domain.model.DueDate;
import com.todo.domain.repository.TaskRepository;
import com.todo.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateTaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskApplicationMapper mapper;

    @Transactional
    public TaskResponse execute(String id, UpdateTaskRequest request) {
        TaskId taskId = TaskId.of(id);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (request.getTitle() != null) {
            task.updateTitle(TaskTitle.of(request.getTitle()));
        }
        if (request.getDescription() != null) {
            task.updateDescription(TaskDescription.of(request.getDescription()));
        }
        if (request.getPriority() != null) {
            task.updatePriority(Priority.valueOf(request.getPriority().name()));
        }
        if (request.getDueDate() != null) {
            task.updateDueDate(DueDate.of(request.getDueDate()));
        }

        Task saved = taskRepository.save(task);
        return mapper.toResponse(saved);
    }
}