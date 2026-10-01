package com.example.todobackend.service;

import com.example.todobackend.dto.DueTodoResponse;
import com.example.todobackend.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class InternalTodoService {
    private final TodoRepository todoRepository;
    public List<DueTodoResponse> getIncompleteTodosDueOn(LocalDate dueDate){
        return todoRepository.findIncompleteByDueDate(dueDate);
    }

}
