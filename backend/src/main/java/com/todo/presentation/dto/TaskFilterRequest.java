package com.todo.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilterRequest {
    private String status; // PENDING, COMPLETED
    private String priority; // LOW, MEDIUM, HIGH
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDir = "desc"; // asc, desc
}