package com.aula.microservices.agenda.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa cualquier evento académico: examen, entrega, charla, taller...
 * targetGroup: a qué clase va dirigido (ej: "DAM1", "DAW2", "TODOS")
 * targetRole: qué rol mínimo puede verlo (ej: "ALUMNO", "PROFESOR")
 * createdBy: email del profesor/admin que lo creó
 */
@Entity
@Table(name = "agenda_events")
public class AgendaEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(length = 100)
    private String targetGroup;

    @Column(length = 50)
    private String targetRole;

    @Column(length = 150)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean active = true;

    public AgendaEvent() {}

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public EventType getType() { return type; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public String getTargetGroup() { return targetGroup; }
    public String getTargetRole() { return targetRole; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Boolean getActive() { return active; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setType(EventType type) { this.type = type; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public void setTargetGroup(String targetGroup) { this.targetGroup = targetGroup; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setActive(Boolean active) { this.active = active; }
}