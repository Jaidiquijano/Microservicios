package com.aula.microservices.tramites.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aula.microservices.tramites.entity.Tramite;
import com.aula.microservices.tramites.entity.TramiteStatus;
import com.aula.microservices.tramites.entity.TramiteType;

public interface TramiteRepository extends JpaRepository<Tramite, Long> {

    // Todos los trámites de un alumno concreto
    List<Tramite> findBySolicitanteEmailOrderByCreatedAtDesc(String email);

    // Trámites de un alumno filtrados por estado
    List<Tramite> findBySolicitanteEmailAndStatusOrderByCreatedAtDesc(
            String email, TramiteStatus status);

    // Todos los trámites pendientes (para secretaría)
    List<Tramite> findByStatusOrderByCreatedAtAsc(TramiteStatus status);

    // Trámites filtrados por tipo
    List<Tramite> findByTypeOrderByCreatedAtDesc(TramiteType type);

    // Todos los trámites (para secretaría/admin)
    List<Tramite> findAllByOrderByCreatedAtDesc();
}