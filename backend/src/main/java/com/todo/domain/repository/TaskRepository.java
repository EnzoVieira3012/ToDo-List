package com.todo.domain.repository;

import com.todo.domain.model.Task;
import com.todo.domain.model.TaskId;
import com.todo.domain.model.TaskStatus;
import com.todo.domain.model.Priority;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(TaskId id);
    List<Task> findAll();
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(Priority priority);
    List<Task> findByStatusAndPriority(TaskStatus status, Priority priority);
    boolean existsById(TaskId id);
    void deleteById(TaskId id);
    long count();
    long countByStatus(TaskStatus status);
}