package com.aula.microservices.users.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        String department,
        Boolean active,
        LocalDateTime createdAt
) {
}
