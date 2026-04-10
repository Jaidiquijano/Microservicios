package com.aula.microservices.notifications.dto;

import java.time.LocalDateTime;

import com.aula.microservices.notifications.entity.NotificationType;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        NotificationType type,
        String recipientEmail,
        String targetRole,
        Boolean read,
        LocalDateTime createdAt,
        String createdBy
) {}