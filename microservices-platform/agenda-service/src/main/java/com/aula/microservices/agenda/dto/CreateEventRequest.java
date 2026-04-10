package com.aula.microservices.agenda.dto;

import com.aula.microservices.agenda.entity.EventType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record CreateEventRequest(

        @NotBlank(message = "El título es obligatorio")
        String title,

        String description,

        @NotNull(message = "El tipo es obligatorio")
        EventType type,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDateTime startDate,

        LocalDateTime endDate,

        // Grupo al que va dirigido: "DAM1", "DAW2", "TODOS"...
        String targetGroup,

        // Rol mínimo que puede verlo: "ALUMNO", "PROFESOR"...
        String targetRole

) {}