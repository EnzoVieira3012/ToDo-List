package com.todo.presentation.mapper;

import com.todo.application.dto.CreateTaskRequest;
import com.todo.application.dto.PaginatedResponse;
import com.todo.application.dto.TaskFilterRequest;
import com.todo.application.dto.TaskResponse;
import com.todo.application.dto.UpdateTaskRequest;
import com.todo.presentation.dto.CreateTaskRequest;
import com.todo.presentation.dto.PaginatedResponse;
import com.todo.presentation.dto.TaskFilterRequest;
import com.todo.presentation.dto.TaskResponse;
import com.todo.presentation.dto.UpdateTaskRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskPresentationMapper {

    TaskPresentationMapper INSTANCE = Mappers.getMapper(TaskPresentationMapper.class);

    @Mapping(target = "priority", source = "priority")
    CreateTaskRequest toApplicationCreate(com.todo.presentation.dto.CreateTaskRequest request);

    @Mapping(target = "priority", source = "priority")
    UpdateTaskRequest toApplicationUpdate(com.todo.presentation.dto.UpdateTaskRequest request);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "priority", source = "priority")
    TaskFilterRequest toApplicationFilter(com.todo.presentation.dto.TaskFilterRequest request);

    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "status", source = "status")
    com.todo.presentation.dto.TaskResponse toPresentationResponse(TaskResponse response);

    <T> com.todo.presentation.dto.PaginatedResponse<T> toPresentationPage(PaginatedResponse<T> response);
}