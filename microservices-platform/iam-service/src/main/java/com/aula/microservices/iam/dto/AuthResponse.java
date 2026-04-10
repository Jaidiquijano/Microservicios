package com.aula.microservices.iam.dto;

public record AuthResponse(
        String token,
        String email,
        String fullName,
        String role,
        long expiresIn
) {}