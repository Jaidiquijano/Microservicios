package com.aula.microservices.agenda.service;

import com.aula.microservices.agenda.dto.CreateEventRequest;
import com.aula.microservices.agenda.dto.EventResponse;
import com.aula.microservices.agenda.entity.AgendaEvent;
import com.aula.microservices.agenda.entity.EventType;
import com.aula.microservices.agenda.repository.AgendaEventRepository;
import com.aula.microservices.common.exection.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaService {

    private final AgendaEventRepository repository;

    public AgendaService(AgendaEventRepository repository) {
        this.repository = repository;
    }

    public EventResponse create(CreateEventRequest request, String createdBy) {
        AgendaEvent event = new AgendaEvent();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setType(request.type());
        event.setStartDate(request.startDate());
        event.setEndDate(request.endDate());
        event.setTargetGroup(request.targetGroup() != null ? request.targetGroup() : "TODOS");
        event.setTargetRole(request.targetRole() != null ? request.targetRole() : "ALUMNO");
        event.setCreatedBy(createdBy);
        return toResponse(repository.save(event));
    }

    public List<EventResponse> findAll() {
        return repository.findByActiveTrueOrderByStartDateAsc()
                .stream().map(this::toResponse).toList();
    }

    public List<EventResponse> findUpcoming() {
        return repository.findByStartDateAfterAndActiveTrueOrderByStartDateAsc(LocalDateTime.now())
                .stream().map(this::toResponse).toList();
    }

    public List<EventResponse> findByType(EventType type) {
        return repository.findByTypeAndActiveTrueOrderByStartDateAsc(type)
                .stream().map(this::toResponse).toList();
    }

    public List<EventResponse> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return repository.findByStartDateBetweenAndActiveTrueOrderByStartDateAsc(from, to)
                .stream().map(this::toResponse).toList();
    }

    public EventResponse findById(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id " + id)));
    }

    public void delete(Long id) {
        AgendaEvent event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id " + id));
        event.setActive(false);
        repository.save(event);
    }

    private EventResponse toResponse(AgendaEvent e) {
        return new EventResponse(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getType(),
                e.getStartDate(),
                e.getEndDate(),
                e.getTargetGroup(),
                e.getTargetRole(),
                e.getCreatedBy(),
                e.getCreatedAt()
        );
    }
}