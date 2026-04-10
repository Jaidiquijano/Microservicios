package com.aula.microservices.notifications.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aula.microservices.common.response.ApiResponse;
import com.aula.microservices.notifications.dto.CreateNotificationRequest;
import com.aula.microservices.notifications.dto.NotificationResponse;
import com.aula.microservices.notifications.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    /**
     * GET /api/notifications/my
     * Devuelve todas las notificaciones relevantes para el usuario autenticado:
     * globales + personales + las de su rol
     * El gateway inyecta X-User-Email y X-User-Role automáticamente
     */
    @GetMapping("/my")
    public ApiResponse<List<NotificationResponse>> myNotifications(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {
        return new ApiResponse<>(true, "Tus notificaciones", service.findForUser(email, role));
    }

    /**
     * GET /api/notifications/my/unread
     * Devuelve solo las notificaciones no leídas del usuario
     * Útil para mostrar el contador de notificaciones en la app
     */
    @GetMapping("/my/unread")
    public ApiResponse<List<NotificationResponse>> myUnread(
            @RequestHeader("X-User-Email") String email) {
        return new ApiResponse<>(true, "Notificaciones no leídas", service.findUnreadForUser(email));
    }

    /**
     * PUT /api/notifications/{id}/read
     * Marcar una notificación como leída
     */
    @PutMapping("/{id}/read")
    public ApiResponse<NotificationResponse> markAsRead(@PathVariable Long id) {
        return new ApiResponse<>(true, "Notificación marcada como leída", service.markAsRead(id));
    }

    /**
     * GET /api/notifications/{id}
     * Detalle de una notificación concreta
     */
    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> findById(@PathVariable Long id) {
        return new ApiResponse<>(true, "Notificación encontrada", service.findById(id));
    }

    /**
     * POST /api/notifications
     * Crear notificación — solo JEFATURA, DIRECCION, SECRETARIA o ADMIN
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<NotificationResponse> create(
            @Valid @RequestBody CreateNotificationRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String userRole) {

        if (!List.of("JEFATURA", "DIRECCION", "SECRETARIA", "ADMIN", "PROFESOR").contains(userRole)) {
            throw new IllegalStateException("No tienes permiso para crear notificaciones");
        }
        return new ApiResponse<>(true, "Notificación creada", service.create(request, userEmail));
    }
}