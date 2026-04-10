package com.aula.microservices.tramites.entity;

public enum TramiteStatus {
    PENDIENTE,    // Recién enviado, esperando revisión
    EN_REVISION,  // Secretaría lo está revisando
    APROBADO,     // Trámite aprobado
    RECHAZADO,    // Trámite rechazado
    COMPLETADO    // Proceso finalizado
}