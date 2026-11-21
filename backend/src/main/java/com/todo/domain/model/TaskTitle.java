package com.todo.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class TaskTitle {
    private static final int MAX_LENGTH = 200;
    private static final int MIN_LENGTH = 1;

    private final String value;

    public static TaskTitle of(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title cannot be null or blank");
        }
        String trimmed = title.trim();
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Task title cannot exceed " + MAX_LENGTH + " characters");
        }
        return new TaskTitle(trimmed);
    }

    @Override
    public String toString() {
        return value;
    }
}