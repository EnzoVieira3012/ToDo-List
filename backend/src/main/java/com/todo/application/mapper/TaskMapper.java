package com.todo.application.mapper;

import com.todo.domain.model.*;
import com.todo.infrastructure.persistence.TaskJpaEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "title.value", source = "title")
    @Mapping(target = "description.value", source = "description")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "dueDate.value", source = "dueDate")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    Task toDomain(TaskJpaEntity entity);

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "title", source = "title.value")
    @Mapping(target = "description", source = "description.value")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "dueDate", source = "dueDate.value")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TaskJpaEntity toEntity(Task domain);

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "title", source = "title.value")
    @Mapping(target = "description", source = "description.value")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "dueDate", source = "dueDate.value")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    void updateEntityFromDomain(Task domain, @MappingTarget TaskJpaEntity entity);
}