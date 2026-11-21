package com.todo.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class TaskId {
    private final UUID value;

    public static TaskId generate() {
        return new TaskId(UUID.randomUUID());
    }

    public static TaskId of(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("TaskId cannot be null");
        }
        return new TaskId(uuid);
    }

    public static TaskId of(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException("TaskId cannot be null or blank");
        }
        return new TaskId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}