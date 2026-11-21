package com.todo.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Priority {
    LOW("Baixa", 1),
    MEDIUM("Média", 2),
    HIGH("Alta", 3);

    private final String label;
    private final int weight;

    public static Priority fromLabel(String label) {
        for (Priority p : values()) {
            if (p.label.equalsIgnoreCase(label)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid priority label: " + label);
    }

    public static Priority fromWeight(int weight) {
        for (Priority p : values()) {
            if (p.weight == weight) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid priority weight: " + weight);
    }

    public boolean isHigherThan(Priority other) {
        return this.weight > other.weight;
    }

    public boolean isLowerThan(Priority other) {
        return this.weight < other.weight;
    }
}