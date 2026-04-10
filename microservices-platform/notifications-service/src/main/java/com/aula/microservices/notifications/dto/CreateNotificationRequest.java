package com.aula.microservices.notifications.dto;

import com.aula.microservices.notifications.entity.NotificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateNotificationRequest(

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "El mensaje es obligatorio")
        String message,

        @NotNull(message = "El tipo es obligatorio")
        NotificationType type,

        // null = global para todos los usuarios
        String recipientEmail,

        // null = para todos los roles
        String targetRole

) {}