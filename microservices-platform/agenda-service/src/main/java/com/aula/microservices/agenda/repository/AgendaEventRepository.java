package com.aula.microservices.agenda.repository;

import com.aula.microservices.agenda.entity.AgendaEvent;
import com.aula.microservices.agenda.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaEventRepository extends JpaRepository<AgendaEvent, Long> {

    // Todos los eventos activos ordenados por fecha
    List<AgendaEvent> findByActiveTrueOrderByStartDateAsc();

    // Filtrar por tipo (EXAMEN, TALLER, etc.)
    List<AgendaEvent> findByTypeAndActiveTrueOrderByStartDateAsc(EventType type);

    // Próximos eventos a partir de ahora
    List<AgendaEvent> findByStartDateAfterAndActiveTrueOrderByStartDateAsc(LocalDateTime from);

    // Eventos en un rango de fechas
    List<AgendaEvent> findByStartDateBetweenAndActiveTrueOrderByStartDateAsc(
            LocalDateTime from, LocalDateTime to);

    // Eventos por grupo (ej: "DAM1") o para "TODOS"
    List<AgendaEvent> findByTargetGroupInAndActiveTrueOrderByStartDateAsc(List<String> groups);
}