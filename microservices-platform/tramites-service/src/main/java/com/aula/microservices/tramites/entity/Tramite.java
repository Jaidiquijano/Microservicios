package com.aula.microservices.tramites.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa cualquier trámite administrativo del centro:
 * justificantes, becas, certificados, convenios...
 *
 * solicitanteEmail: email del alumno/usuario que lo solicita
 * revisadoPor: email del secretario/admin que lo gestiona
 * documentoUrl: ruta o URL del documento adjunto (si lo hay)
 */
@Entity
@Table(name = "tramites")
public class Tramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TramiteType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TramiteStatus status = TramiteStatus.PENDIENTE;

    @Column(nullable = false, length = 150)
    private String solicitanteEmail;

    @Column(nullable = false, length = 500)
    private String descripcion;

    // Email del secretario/admin que revisa el trámite
    @Column(length = 150)
    private String revisadoPor;

    // Motivo de rechazo si status = RECHAZADO
    @Column(length = 500)
    private String motivoRechazo;

    // URL o ruta del documento adjunto
    @Column(length = 500)
    private String documentoUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public Tramite() {}

    // Getters
    public Long getId() { return id; }
    public TramiteType getType() { return type; }
    public TramiteStatus getStatus() { return status; }
    public String getSolicitanteEmail() { return solicitanteEmail; }
    public String getDescripcion() { return descripcion; }
    public String getRevisadoPor() { return revisadoPor; }
    public String getMotivoRechazo() { return motivoRechazo; }
    public String getDocumentoUrl() { return documentoUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setType(TramiteType type) { this.type = type; }
    public void setStatus(TramiteStatus status) { this.status = status; }
    public void setSolicitanteEmail(String solicitanteEmail) { this.solicitanteEmail = solicitanteEmail; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setRevisadoPor(String revisadoPor) { this.revisadoPor = revisadoPor; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
    public void setDocumentoUrl(String documentoUrl) { this.documentoUrl = documentoUrl; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}