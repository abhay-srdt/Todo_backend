package com.example.todobackend.service;


import com.example.todobackend.entity.Todo;
import com.example.todobackend.entity.User;
import com.example.todobackend.repository.TodoRepository;
import com.example.todobackend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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
    public List<Todo> getAllTodos(){
        return todoRepository.findAll();
    }
    @PreAuthorize("#userId == authentication.principal.id")
    public List<Todo> getTodosByUser(Long userId) {

        return todoRepository.findByUserId(userId);
    }
    public Todo createTodo(Long userId,Todo todo) {
        todo.setDate(LocalDate.now());
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found."));
        todo.setUser(user);
        return todoRepository.save(todo);
    }
    public Todo updateTodo(Long id, Todo updatedTodo) {

        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        existingTodo.setDate(updatedTodo.getDate());
        existingTodo.setTitle(updatedTodo.getTitle());
        existingTodo.setDescription(updatedTodo.getDescription());

        return todoRepository.save(existingTodo);
    }
    public List<Todo> getTodosByDate(LocalDate date, Long userId) {
        return todoRepository.findByDateAndUserId(date,userId);
    }
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
