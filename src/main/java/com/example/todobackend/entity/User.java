package com.example.todobackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "users")
public class User {
    public User() {
    }
    private String role = "USER";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Todo> todos;
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Getter
    private String password;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    @Column(unique = true)
    private String email;

    public void setPassword(String password) {
        this.password = password;
    }
}
