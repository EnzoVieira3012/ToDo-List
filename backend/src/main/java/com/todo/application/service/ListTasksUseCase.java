package com.todo.application.service;

import com.todo.application.dto.PaginatedResponse;
import com.todo.application.dto.TaskFilterRequest;
import com.todo.application.dto.TaskResponse;
import com.todo.application.mapper.TaskApplicationMapper;
import com.todo.domain.model.Priority;
import com.todo.domain.model.Task;
import com.todo.domain.model.TaskStatus;
import com.todo.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListTasksUseCase {

    private final TaskRepository taskRepository;
    private final TaskApplicationMapper mapper;

    @Transactional(readOnly = true)
    public PaginatedResponse<TaskResponse> execute(TaskFilterRequest filter) {
        TaskStatus status = filter.getStatus() != null ? TaskStatus.valueOf(filter.getStatus()) : null;
        Priority priority = filter.getPriority() != null ? Priority.valueOf(filter.getPriority()) : null;

        Page<Task> page = taskRepository.findAllPaged(
                filter.getPage(),
                filter.getSize(),
                filter.getSortBy(),
                filter.getSortDir()
        );

        if (status != null && priority != null) {
            page = taskRepository.findByStatusAndPriorityPaged(
                    status, priority, filter.getPage(), filter.getSize(), filter.getSortBy(), filter.getSortDir());
        } else if (status != null) {
            page = taskRepository.findByStatusPaged(
                    status, filter.getPage(), filter.getSize(), filter.getSortBy(), filter.getSortDir());
        } else if (priority != null) {
            page = taskRepository.findByPriorityPaged(
                    priority, filter.getPage(), filter.getSize(), filter.getSortBy(), filter.getSortDir());
        }

        return PaginatedResponse.<TaskResponse>builder()
                .content(page.getContent().stream().map(mapper::toResponse).collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}