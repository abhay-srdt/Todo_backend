package com.example.todobackend.repository;

import com.example.todobackend.dto.DueTodoResponse;
import com.example.todobackend.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findByUserId(Long userId);

    List<Todo> findByDateAndUserId( LocalDate date,Long userId);
    @Query("""
            select new com.example.todobackend.dto.DueTodoResponse(
            t.id, t.title, t.description, t.dueDate, u.name, u.email)
            from Todo t join t.user u
            where t.dueDate = :dueDate
            and t.completed = false
            and u.email is not null
          """)
    List<DueTodoResponse>findIncompleteByDueDate(@Param("dueDate") LocalDate dueDate);
}
