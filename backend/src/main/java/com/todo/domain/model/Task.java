package com.todo.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Task {
    private final TaskId id;
    private TaskTitle title;
    private TaskDescription description;
    private Priority priority;
    private TaskStatus status;
    private DueDate dueDate;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Task(TaskId id, TaskTitle title, TaskDescription description, Priority priority, DueDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Task create(TaskTitle title, TaskDescription description, Priority priority, DueDate dueDate) {
        return new Task(TaskId.generate(), title, description, priority, dueDate);
    }

    public static Task reconstruct(TaskId id, TaskTitle title, TaskDescription description, Priority priority,
                                    TaskStatus status, DueDate dueDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Task task = new Task(id, title, description, priority, dueDate);
        task.status = status;
        task.createdAt = createdAt;
        task.updatedAt = updatedAt;
        return task;
    }

    public void updateTitle(TaskTitle newTitle) {
        this.title = newTitle;
        touch();
    }

    public void updateDescription(TaskDescription newDescription) {
        this.description = newDescription;
        touch();
    }

    public void updatePriority(Priority newPriority) {
        this.priority = newPriority;
        touch();
    }

    public void updateDueDate(DueDate newDueDate) {
        this.dueDate = newDueDate;
        touch();
    }

    public void complete() {
        if (this.status == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Task is already completed");
        }
        this.status = TaskStatus.COMPLETED;
        touch();
    }

    public void reopen() {
        if (this.status == TaskStatus.PENDING) {
            throw new IllegalStateException("Task is already pending");
        }
        this.status = TaskStatus.PENDING;
        touch();
    }

    public void toggleStatus() {
        this.status = this.status.toggle();
        touch();
    }

    public boolean isOverdue() {
        return dueDate != null && dueDate.isOverdue() && status == TaskStatus.PENDING;
    }

    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    public boolean isPending() {
        return status == TaskStatus.PENDING;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}