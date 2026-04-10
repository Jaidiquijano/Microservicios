package com.aula.microservices.tramites.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aula.microservices.common.exection.ResourceNotFoundException;
import com.aula.microservices.tramites.dto.CreateTramiteRequest;
import com.aula.microservices.tramites.dto.TramiteResponse;
import com.aula.microservices.tramites.dto.UpdateTramiteStatusRequest;
import com.aula.microservices.tramites.entity.Tramite;
import com.aula.microservices.tramites.entity.TramiteStatus;
import com.aula.microservices.tramites.repository.TramiteRepository;

@Service
public class TramiteService {

    private final TramiteRepository repository;

    public TramiteService(TramiteRepository repository) {
        this.repository = repository;
    }

    /**
     * Crear un nuevo trámite — cualquier usuario autenticado puede solicitarlo
     */
    public TramiteResponse create(CreateTramiteRequest request, String solicitanteEmail) {
        Tramite tramite = new Tramite();
        tramite.setType(request.type());
        tramite.setDescripcion(request.descripcion());
        tramite.setDocumentoUrl(request.documentoUrl());
        tramite.setSolicitanteEmail(solicitanteEmail);
        tramite.setStatus(TramiteStatus.PENDIENTE);
        return toResponse(repository.save(tramite));
    }

    /**
     * Ver mis trámites — el alumno ve solo los suyos
     */
    public List<TramiteResponse> findMine(String email) {
        return repository.findBySolicitanteEmailOrderByCreatedAtDesc(email)
                .stream().map(this::toResponse).toList();
    }

    /**
     * Ver todos los trámites — solo SECRETARIA, JEFATURA, DIRECCION, ADMIN
     */
    public List<TramiteResponse> findAll() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    /**
     * Ver trámites pendientes — para secretaría
     */
    public List<TramiteResponse> findPending() {
        return repository.findByStatusOrderByCreatedAtAsc(TramiteStatus.PENDIENTE)
                .stream().map(this::toResponse).toList();
    }

    /**
     * Ver detalle de un trámite concreto
     */
    public TramiteResponse findById(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite no encontrado con id " + id)));
    }

    /**
     * Actualizar estado — solo SECRETARIA, JEFATURA, DIRECCION, ADMIN
     */
    public TramiteResponse updateStatus(Long id, UpdateTramiteStatusRequest request, String revisadoPor) {
        Tramite tramite = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite no encontrado con id " + id));

        if (request.status() == TramiteStatus.RECHAZADO && 
            (request.motivoRechazo() == null || request.motivoRechazo().isBlank())) {
            throw new IllegalArgumentException("Debes indicar el motivo de rechazo");
        }

        tramite.setStatus(request.status());
        tramite.setRevisadoPor(revisadoPor);
        tramite.setMotivoRechazo(request.motivoRechazo());
        tramite.setUpdatedAt(LocalDateTime.now());

        return toResponse(repository.save(tramite));
    }

    private TramiteResponse toResponse(Tramite t) {
        return new TramiteResponse(
                t.getId(),
                t.getType(),
                t.getStatus(),
                t.getSolicitanteEmail(),
                t.getDescripcion(),
                t.getRevisadoPor(),
                t.getMotivoRechazo(),
                t.getDocumentoUrl(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}