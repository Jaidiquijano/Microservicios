package com.aula.microservices.tramites.dto;

import com.aula.microservices.tramites.entity.TramiteType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTramiteRequest(

        @NotNull(message = "El tipo de trámite es obligatorio")
        TramiteType type,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        // URL del documento adjunto (opcional)
        String documentoUrl

) {}