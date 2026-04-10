package com.aula.microservices.tramites.controller;

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
import com.aula.microservices.tramites.dto.CreateTramiteRequest;
import com.aula.microservices.tramites.dto.TramiteResponse;
import com.aula.microservices.tramites.dto.UpdateTramiteStatusRequest;
import com.aula.microservices.tramites.service.TramiteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tramites")
public class TramiteController {

    private final TramiteService service;

    public TramiteController(TramiteService service) {
        this.service = service;
    }

    /**
     * POST /api/tramites
     * Crear un nuevo trámite — cualquier usuario autenticado
     * Tipos: JUSTIFICANTE, BECA, CERTIFICADO, CONVENIO, MATRICULA, OTRO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TramiteResponse> create(
            @Valid @RequestBody CreateTramiteRequest request,
            @RequestHeader("X-User-Email") String userEmail) {
        return new ApiResponse<>(true, "Trámite creado correctamente", service.create(request, userEmail));
    }

    /**
     * GET /api/tramites/my
     * Ver mis propios trámites — el alumno ve solo los suyos
     */
    @GetMapping("/my")
    public ApiResponse<List<TramiteResponse>> myTramites(
            @RequestHeader("X-User-Email") String userEmail) {
        return new ApiResponse<>(true, "Tus trámites", service.findMine(userEmail));
    }

    /**
     * GET /api/tramites
     * Ver todos los trámites — solo SECRETARIA, JEFATURA, DIRECCION, ADMIN
     */
    @GetMapping
    public ApiResponse<List<TramiteResponse>> findAll(
            @RequestHeader("X-User-Role") String userRole) {
        if (!List.of("SECRETARIA", "JEFATURA", "DIRECCION", "ADMIN").contains(userRole)) {
            throw new IllegalStateException("No tienes permiso para ver todos los trámites");
        }
        return new ApiResponse<>(true, "Todos los trámites", service.findAll());
    }

    /**
     * GET /api/tramites/pending
     * Ver trámites pendientes — solo SECRETARIA, JEFATURA, DIRECCION, ADMIN
     */
    @GetMapping("/pending")
    public ApiResponse<List<TramiteResponse>> findPending(
            @RequestHeader("X-User-Role") String userRole) {
        if (!List.of("SECRETARIA", "JEFATURA", "DIRECCION", "ADMIN").contains(userRole)) {
            throw new IllegalStateException("No tienes permiso para ver los trámites pendientes");
        }
        return new ApiResponse<>(true, "Trámites pendientes", service.findPending());
    }

    /**
     * GET /api/tramites/{id}
     * Ver detalle de un trámite concreto
     */
    @GetMapping("/{id}")
    public ApiResponse<TramiteResponse> findById(@PathVariable Long id) {
        return new ApiResponse<>(true, "Trámite encontrado", service.findById(id));
    }

    /**
     * PUT /api/tramites/{id}/status
     * Actualizar estado — solo SECRETARIA, JEFATURA, DIRECCION, ADMIN
     * Estados: PENDIENTE, EN_REVISION, APROBADO, RECHAZADO, COMPLETADO
     */
    @PutMapping("/{id}/status")
    public ApiResponse<TramiteResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTramiteStatusRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String userRole) {
        if (!List.of("SECRETARIA", "JEFATURA", "DIRECCION", "ADMIN").contains(userRole)) {
            throw new IllegalStateException("No tienes permiso para actualizar el estado de un trámite");
        }
        return new ApiResponse<>(true, "Estado actualizado", service.updateStatus(id, request, userEmail));
    }
}