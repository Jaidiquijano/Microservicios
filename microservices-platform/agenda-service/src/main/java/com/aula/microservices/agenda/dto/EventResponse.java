package com.aula.microservices.agenda.dto;

import com.aula.microservices.agenda.entity.EventType;
import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String title,
        String description,
        EventType type,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String targetGroup,
        String targetRole,
        String createdBy,
        LocalDateTime createdAt
) {}