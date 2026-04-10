package com.aula.microservices.tramites.dto;

import java.time.LocalDateTime;

import com.aula.microservices.tramites.entity.TramiteStatus;
import com.aula.microservices.tramites.entity.TramiteType;

public record TramiteResponse(
        Long id,
        TramiteType type,
        TramiteStatus status,
        String solicitanteEmail,
        String descripcion,
        String revisadoPor,
        String motivoRechazo,
        String documentoUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}