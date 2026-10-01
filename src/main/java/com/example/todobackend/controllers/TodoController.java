package com.example.todobackend.controllers;

import com.example.todobackend.dto.CompletedRequest;
import com.example.todobackend.entity.Todo;
import com.example.todobackend.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/todos")
@RestController
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/date/{userId}/{date}")
    public ResponseEntity<List<Todo>> getTodosByDate(
            @PathVariable LocalDate date,
            @PathVariable Long userId,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(todoService.getTodosByDate(date, userId, jwt.getSubject()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Todo>> getTodosByUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(todoService.getTodosByUser(userId, jwt.getSubject()));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Todo> createTodo(
            @PathVariable Long userId,
            @RequestBody Todo todo,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(todoService.createTodo(userId, todo, jwt.getSubject()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable Long id,
            @RequestBody Todo updatedTodo,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(todoService.updateTodo(id, updatedTodo, jwt.getSubject()));
    }

    // Body: { "completed": true }
    @PutMapping("/{id}/completed")
    public ResponseEntity<Todo> setCompleted(
            @PathVariable Long id,
            @RequestBody CompletedRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(todoService.setCompleted(id, request.completed(), jwt.getSubject()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        todoService.deleteTodo(id, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }
}