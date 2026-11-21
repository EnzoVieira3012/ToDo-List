package com.todo.infrastructure.persistence;

import com.todo.application.mapper.TaskMapper;
import com.todo.domain.model.*;
import com.todo.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final SpringDataTaskRepository springDataRepository;
    private final TaskMapper mapper;

    @Override
    public Task save(Task task) {
        TaskJpaEntity entity = mapper.toEntity(task);
        TaskJpaEntity saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findById(TaskId id) {
        return springDataRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return springDataRepository.findByStatus(status.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByPriority(Priority priority) {
        return springDataRepository.findByPriority(priority.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByStatusAndPriority(TaskStatus status, Priority priority) {
        return springDataRepository.findByStatusAndPriority(status.name(), priority.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(TaskId id) {
        return springDataRepository.existsById(id.getValue());
    }

    @Override
    public void deleteById(TaskId id) {
        springDataRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return springDataRepository.count();
    }

    @Override
    public long countByStatus(TaskStatus status) {
        return springDataRepository.countByStatus(status.name());
    }

    public Page<Task> findAllPaged(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return springDataRepository.findAllPaged(pageable).map(mapper::toDomain);
    }

    public Page<Task> findByStatusPaged(TaskStatus status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return springDataRepository.findByStatus(status.name(), pageable).map(mapper::toDomain);
    }

    public Page<Task> findByPriorityPaged(Priority priority, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return springDataRepository.findByPriority(priority.name(), pageable).map(mapper::toDomain);
    }

    public Page<Task> findByStatusAndPriorityPaged(TaskStatus status, Priority priority, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return springDataRepository.findByStatusAndPriority(status.name(), priority.name(), pageable).map(mapper::toDomain);
    }
}