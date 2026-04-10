package com.aula.microservices.tramites.dto;

import com.aula.microservices.tramites.entity.TramiteStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateTramiteStatusRequest(

        @NotNull(message = "El estado es obligatorio")
        TramiteStatus status,

        // Obligatorio solo si status = RECHAZADO
        String motivoRechazo

) {}