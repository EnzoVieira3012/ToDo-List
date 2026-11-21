package com.todo.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class TaskDescription {
    private static final int MAX_LENGTH = 1000;

    private final String value;

    public static TaskDescription of(String description) {
        if (description == null) {
            return new TaskDescription(null);
        }
        String trimmed = description.trim();
        if (trimmed.isEmpty()) {
            return new TaskDescription(null);
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Task description cannot exceed " + MAX_LENGTH + " characters");
        }
        return new TaskDescription(trimmed);
    }

    public boolean isPresent() {
        return value != null && !value.isBlank();
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}