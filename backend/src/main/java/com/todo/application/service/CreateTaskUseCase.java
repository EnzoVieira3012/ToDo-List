package com.todo.application.service;

import com.todo.application.dto.CreateTaskRequest;
import com.todo.application.dto.TaskResponse;
import com.todo.application.mapper.TaskApplicationMapper;
import com.todo.domain.model.Task;
import com.todo.domain.model.TaskDescription;
import com.todo.domain.model.TaskTitle;
import com.todo.domain.model.Priority;
import com.todo.domain.model.DueDate;
import com.todo.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskApplicationMapper mapper;

    @Transactional
    public TaskResponse execute(CreateTaskRequest request) {
        TaskTitle title = TaskTitle.of(request.getTitle());
        TaskDescription description = TaskDescription.of(request.getDescription());
        Priority priority = Priority.valueOf(request.getPriority().name());
        DueDate dueDate = DueDate.of(request.getDueDate());

        Task task = Task.create(title, description, priority, dueDate);
        Task saved = taskRepository.save(task);
        return mapper.toResponse(saved);
    }
}