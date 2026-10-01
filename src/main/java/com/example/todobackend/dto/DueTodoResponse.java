package com.example.todobackend.dto;

import java.time.LocalDate;

public record DueTodoResponse(
        Long todoId,
        String title,
        String description,
        LocalDate dueDate,
        String userName,
        String userEmail
) {
}