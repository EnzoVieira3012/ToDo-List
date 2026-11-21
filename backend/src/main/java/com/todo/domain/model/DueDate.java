package com.todo.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class DueDate {
    private final LocalDate value;

    public static DueDate of(LocalDate date) {
        if (date == null) {
            return new DueDate(null);
        }
        return new DueDate(date);
    }

    public static DueDate of(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return new DueDate(null);
        }
        return new DueDate(LocalDate.parse(dateStr));
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isOverdue() {
        return value != null && value.isBefore(LocalDate.now());
    }

    public boolean isToday() {
        return value != null && value.equals(LocalDate.now());
    }

    public boolean isFuture() {
        return value != null && value.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "";
    }
}