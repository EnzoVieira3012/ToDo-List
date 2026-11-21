package com.todo.application.service;

import com.todo.domain.model.TaskId;
import com.todo.domain.repository.TaskRepository;
import com.todo.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteTaskUseCase {

    private final TaskRepository taskRepository;

    @Transactional
    public void execute(String id) {
        TaskId taskId = TaskId.of(id);
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }
        taskRepository.deleteById(taskId);
    }
}