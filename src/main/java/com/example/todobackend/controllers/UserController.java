package com.example.todobackend.controllers;


import com.example.todobackend.dto.AuthResponse;
import com.example.todobackend.dto.LoginDto;
import com.example.todobackend.dto.UserResponse;
import com.example.todobackend.entity.User;
import com.example.todobackend.security.JwtService;
import com.example.todobackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User user) {

        try {
            User savedUser = userService.register(user);
            return ResponseEntity.ok(savedUser);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message",e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto request) {


        try {
            Authentication authResult = userService.authenticateUser(request.getEmail(),request.getPassword());
            String token = jwtService.generateToken(request.getEmail());
            User user = userService.getByEmail(request.getEmail());
            AuthResponse response = new AuthResponse(user.getId(),user.getName(), user.getEmail(),token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401
                    .body(Map.of("message", e.getMessage()));
        }
    }

}
