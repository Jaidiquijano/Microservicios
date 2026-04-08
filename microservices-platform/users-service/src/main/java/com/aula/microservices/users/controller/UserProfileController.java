package com.aula.microservices.users.controller;

import com.aula.microservices.common.response.ApiResponse;
import com.aula.microservices.users.dto.CreateUserRequest;
import com.aula.microservices.users.dto.UserResponse;
import com.aula.microservices.users.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return new ApiResponse<>(true, "Usuario creado correctamente", service.create(request));
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> findAll() {
        return new ApiResponse<>(true, "Listado de usuarios", service.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable Long id) {
        return new ApiResponse<>(true, "Usuario encontrado", service.findById(id));
    }
}
