package com.example.todobackend.controllers;

import com.example.todobackend.entity.User;
import com.example.todobackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/users")
@RestController
public class CurrentUserController {

    private final UserRepository userRepository;

    public CurrentUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(jwt.getClaimAsString("name"));
            return userRepository.save(newUser);
        });

        String displayName = (user.getName() == null || user.getName().isBlank())
                ? email : user.getName();

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", displayName,
                "email", user.getEmail()
        ));
    }
}