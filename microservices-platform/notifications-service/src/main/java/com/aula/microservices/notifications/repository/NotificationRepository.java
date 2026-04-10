package com.aula.microservices.notifications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aula.microservices.notifications.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Notificaciones globales (sin destinatario concreto)
    List<Notification> findByRecipientEmailIsNullOrderByCreatedAtDesc();

    // Notificaciones personales de un usuario concreto
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String email);

    // Notificaciones no leídas de un usuario concreto
    List<Notification> findByRecipientEmailAndReadFalseOrderByCreatedAtDesc(String email);

    // Notificaciones globales no leídas
    List<Notification> findByRecipientEmailIsNullAndReadFalseOrderByCreatedAtDesc();

    // Notificaciones dirigidas a un rol concreto
    List<Notification> findByTargetRoleOrderByCreatedAtDesc(String targetRole);
}