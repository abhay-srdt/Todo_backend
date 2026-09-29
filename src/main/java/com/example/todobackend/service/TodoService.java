package com.example.todobackend.service;


import com.example.todobackend.entity.Todo;
import com.example.todobackend.entity.User;
import com.example.todobackend.repository.TodoRepository;
import com.example.todobackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    // Admin-only, enforced in SecurityConfig
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public List<Todo> getTodosByUser(Long userId, String email) {
        requireSelf(userId, email);
        return todoRepository.findByUserId(userId);
    }

    public List<Todo> getTodosByDate(LocalDate date, Long userId, String email) {
        requireSelf(userId, email);
        return todoRepository.findByDateAndUserId(date, userId);
    }

    public Todo createTodo(Long userId, Todo request, String email) {
        User user = requireSelf(userId, email);

        // Build a fresh Todo: never save the request body directly
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDueDate(request.getDueDate());
        todo.setDate(LocalDate.now());
        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, Todo updatedTodo, String email) {
        Todo existingTodo = findOwnedTodo(id, email);

        if (updatedTodo.getDate() != null) {
            existingTodo.setDate(updatedTodo.getDate());
        }
        existingTodo.setTitle(updatedTodo.getTitle());
        existingTodo.setDescription(updatedTodo.getDescription());
        existingTodo.setDueDate(updatedTodo.getDueDate());

        return todoRepository.save(existingTodo);
    }

    public Todo setCompleted(Long id, boolean completed, String email) {
        Todo todo = findOwnedTodo(id, email);
        todo.setCompleted(completed);
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id, String email) {
        Todo todo = findOwnedTodo(id, email);
        todoRepository.delete(todo);
    }

    // ---------- helpers ----------

    private User requireCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No local user record"));
    }

    // The {userId} in the URL must belong to the person holding the token
    private User requireSelf(Long userId, String email) {
        User user = requireCurrentUser(email);
        if (!user.getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your own todos");
        }
        return user;
    }

    private Todo findOwnedTodo(Long id, String email) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
        if (todo.getUser() == null || !email.equals(todo.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this todo");
        }
        return todo;
    }
}