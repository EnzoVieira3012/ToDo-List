package com.todo.application.mapper;

import com.todo.application.dto.TaskResponse;
import com.todo.application.dto.CreateTaskRequest;
import com.todo.application.dto.UpdateTaskRequest;
import com.todo.domain.model.Task;
import com.todo.domain.model.Priority;
import com.todo.domain.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskApplicationMapper {

    TaskApplicationMapper INSTANCE = Mappers.getMapper(TaskApplicationMapper.class);

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "title", source = "title.value")
    @Mapping(target = "description", source = "description.value")
    @Mapping(target = "priority", source = "priority.name")
    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "dueDate", source = "dueDate.value")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "overdue", source = "overdue")
    TaskResponse toResponse(Task task);

    Priority toDomainPriority(CreateTaskRequest.PriorityDto priority);

    Priority toDomainPriority(UpdateTaskRequest.PriorityDto priority);

    TaskStatus toDomainStatus(String status);
}