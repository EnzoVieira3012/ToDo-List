package com.todo.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    PENDING("Pendente"),
    COMPLETED("Concluída");

    private final String label;

    public static TaskStatus fromLabel(String label) {
        for (TaskStatus s : values()) {
            if (s.label.equalsIgnoreCase(label)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status label: " + label);
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public TaskStatus toggle() {
        return this == PENDING ? COMPLETED : PENDING;
    }
}