package com.aula.microservices.notifications.entity;

public enum NotificationType {
    AVISO,          // Aviso general del centro
    RECORDATORIO,   // Recordatorio de examen, entrega, etc.
    CAMBIO_HORARIO, // Cambio de última hora
    EVENTO,         // Notificación relacionada con un evento
    TRAMITE,        // Actualización de estado de un trámite
    INCIDENCIA,     // Comunicación de incidencia
    OTRO
}