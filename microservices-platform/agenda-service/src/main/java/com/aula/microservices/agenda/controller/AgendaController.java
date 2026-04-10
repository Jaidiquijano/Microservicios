package com.aula.microservices.agenda.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aula.microservices.agenda.dto.CreateEventRequest;
import com.aula.microservices.agenda.dto.EventResponse;
import com.aula.microservices.agenda.entity.EventType;
import com.aula.microservices.agenda.service.AgendaService;
import com.aula.microservices.common.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    private final AgendaService service;

    public AgendaController(AgendaService service) {
        this.service = service;
    }

    /**
     * GET /api/agenda
     * Devuelve todos los eventos activos ordenados por fecha
     * Accesible para cualquier usuario autenticado
     */
    @GetMapping
    public ApiResponse<List<EventResponse>> findAll() {
        return new ApiResponse<>(true, "Eventos obtenidos", service.findAll());
    }

    /**
     * GET /api/agenda/upcoming
     * Devuelve solo los eventos futuros (a partir de ahora)
     * Útil para el panel principal del alumno
     */
    @GetMapping("/upcoming")
    public ApiResponse<List<EventResponse>> upcoming() {
        return new ApiResponse<>(true, "Próximos eventos", service.findUpcoming());
    }

    /**
     * GET /api/agenda/type/{type}
     * Filtra por tipo: EXAMEN, ENTREGA, TALLER, CHARLA, EXCURSION, FESTIVO, OTRO
     */
    @GetMapping("/type/{type}")
    public ApiResponse<List<EventResponse>> byType(@PathVariable EventType type) {
        return new ApiResponse<>(true, "Eventos por tipo", service.findByType(type));
    }

    /**
     * GET /api/agenda/range?from=2025-01-01T00:00:00&to=2025-06-30T23:59:59
     * Filtra por rango de fechas
     */
    @GetMapping("/range")
    public ApiResponse<List<EventResponse>> byRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return new ApiResponse<>(true, "Eventos en rango", service.findByDateRange(from, to));
    }

    /**
     * GET /api/agenda/{id}
     * Detalle de un evento concreto
     */
    @GetMapping("/{id}")
    public ApiResponse<EventResponse> findById(@PathVariable Long id) {
        return new ApiResponse<>(true, "Evento encontrado", service.findById(id));
    }

    /**
     * POST /api/agenda
     * Crear evento — solo PROFESOR, JEFATURA, DIRECCION o ADMIN
     * El gateway inyecta X-User-Email y X-User-Role automáticamente
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EventResponse> create(
            @Valid @RequestBody CreateEventRequest request,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String userRole) {

        if (!List.of("PROFESOR", "JEFATURA", "DIRECCION", "ADMIN").contains(userRole)) {
            throw new IllegalStateException("No tienes permiso para crear eventos");
        }
        return new ApiResponse<>(true, "Evento creado", service.create(request, userEmail));
    }

    /**
     * DELETE /api/agenda/{id}
     * Borrado lógico — solo JEFATURA, DIRECCION o ADMIN
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String userRole) {

        if (!List.of("JEFATURA", "DIRECCION", "ADMIN").contains(userRole)) {
            throw new IllegalStateException("No tienes permiso para eliminar eventos");
        }
        service.delete(id);
    }
}