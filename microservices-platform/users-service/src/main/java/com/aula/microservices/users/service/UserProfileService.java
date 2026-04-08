package com.aula.microservices.users.service;




import com.aula.microservices.users.dto.CreateUserRequest;
import com.aula.microservices.users.dto.UserResponse;
import com.aula.microservices.users.entity.UserProfile;
import com.aula.microservices.users.exception.ResourceNotFoundException;
import com.aula.microservices.users.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public UserResponse create(CreateUserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        UserProfile user = new UserProfile(
                request.fullName(),
                request.email(),
                request.department(),
                true
        );

        UserProfile saved = repository.save(user);
        return toResponse(saved);
    }

    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        UserProfile user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        return toResponse(user);
    }

    private UserResponse toResponse(UserProfile user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getDepartment(),
                user.getActive(),
                user.getCreatedAt()
        );
    }
}