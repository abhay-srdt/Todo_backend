package com.example.todobackend.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}