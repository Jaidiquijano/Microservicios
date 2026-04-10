package com.aula.microservices.notifications.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representa una notificación del sistema.
 * Puede ser global (para todos) o personal (para un usuario concreto).
 * Si recipientEmail es null → notificación global para todos.
 * Si targetRole es null → va a todos los roles.
 */
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // null = notificación global para todos los usuarios
    @Column(length = 150)
    private String recipientEmail;

    // null = va a todos los roles
    @Column(length = 50)
    private String targetRole;

    @Column(nullable = false)
    private Boolean read = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(length = 150)
    private String createdBy;

    public Notification() {}

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public NotificationType getType() { return type; }
    public String getRecipientEmail() { return recipientEmail; }
    public String getTargetRole() { return targetRole; }
    public Boolean getRead() { return read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedBy() { return createdBy; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setMessage(String message) { this.message = message; }
    public void setType(NotificationType type) { this.type = type; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    public void setRead(Boolean read) { this.read = read; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}