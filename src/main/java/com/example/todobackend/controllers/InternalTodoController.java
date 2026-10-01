package com.example.todobackend.controllers;

import com.example.todobackend.dto.DueTodoResponse;
import com.example.todobackend.service.InternalTodoService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/internal/todos")
@AllArgsConstructor
public class InternalTodoController {
    private final InternalTodoService internalTodoService;
    @GetMapping("/due")
    public List<DueTodoResponse> getDue(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate date
            ){
        return internalTodoService.getIncompleteTodosDueOn(date);
    }
}
