package com.example.todobackend.controllers;


import com.example.todobackend.entity.Todo;
import com.example.todobackend.security.CustomUserDetails;
import com.example.todobackend.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    public List<Todo> getAllTodos(){
        return todoService.getAllTodos();
    }
    @GetMapping("/date/{userId}/{date}")
    public ResponseEntity<List<Todo>> getTodosByDate(
            @PathVariable LocalDate date,@PathVariable Long userId) {

        List<Todo> todos = todoService.getTodosByDate(date,userId);

        return ResponseEntity.ok(todos);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Todo>> getTodosByUser(
            @PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if(!currentUser.getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(
                todoService.getTodosByUser(userId)
        );
    }
    @PostMapping("/user/{userId}")
    public ResponseEntity<Todo> createTodo(@PathVariable Long userId, @RequestBody Todo todo){
        return ResponseEntity.ok(
                todoService.createTodo(userId, todo)
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable Long id,
            @RequestBody Todo updatedTodo) {

        Todo savedTodo = todoService.updateTodo(id, updatedTodo);

        return ResponseEntity.ok(savedTodo);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {

        todoService.deleteTodo(id);

        return ResponseEntity.noContent().build();
    }

}
