package com.aula.microservices.notifications.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aula.microservices.common.exection.ResourceNotFoundException;
import com.aula.microservices.notifications.dto.CreateNotificationRequest;
import com.aula.microservices.notifications.dto.NotificationResponse;
import com.aula.microservices.notifications.entity.Notification;
import com.aula.microservices.notifications.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public NotificationResponse create(CreateNotificationRequest request, String createdBy) {
        Notification notification = new Notification();
        notification.setTitle(request.title());
        notification.setMessage(request.message());
        notification.setType(request.type());
        notification.setRecipientEmail(request.recipientEmail());
        notification.setTargetRole(request.targetRole());
        notification.setCreatedBy(createdBy);
        return toResponse(repository.save(notification));
    }

    /**
     * Devuelve todas las notificaciones relevantes para un usuario:
     * - Las globales (sin destinatario)
     * - Las personales dirigidas a su email
     * - Las dirigidas a su rol
     */
    public List<NotificationResponse> findForUser(String email, String role) {
        List<Notification> result = new ArrayList<>();
        result.addAll(repository.findByRecipientEmailIsNullOrderByCreatedAtDesc());
        result.addAll(repository.findByRecipientEmailOrderByCreatedAtDesc(email));
        if (role != null) {
            result.addAll(repository.findByTargetRoleOrderByCreatedAtDesc(role));
        }
        // Eliminar duplicados y ordenar por fecha descendente
        return result.stream()
                .distinct()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toResponse)
                .toList();
    }

    /**
     * Devuelve solo las notificaciones no leídas del usuario
     */
    public List<NotificationResponse> findUnreadForUser(String email) {
        List<Notification> result = new ArrayList<>();
        result.addAll(repository.findByRecipientEmailIsNullAndReadFalseOrderByCreatedAtDesc());
        result.addAll(repository.findByRecipientEmailAndReadFalseOrderByCreatedAtDesc(email));
        return result.stream()
                .distinct()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toResponse)
                .toList();
    }

    /**
     * Marcar una notificación como leída
     */
    public NotificationResponse markAsRead(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id " + id));
        notification.setRead(true);
        return toResponse(repository.save(notification));
    }

    public NotificationResponse findById(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id " + id)));
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.getType(),
                n.getRecipientEmail(),
                n.getTargetRole(),
                n.getRead(),
                n.getCreatedAt(),
                n.getCreatedBy()
        );
    }
}