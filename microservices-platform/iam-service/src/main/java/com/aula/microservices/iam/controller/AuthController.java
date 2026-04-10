package com.aula.microservices.iam.controller;

import com.aula.microservices.common.response.ApiResponse;
import com.aula.microservices.iam.dto.AuthResponse;
import com.aula.microservices.iam.dto.LoginRequest;
import com.aula.microservices.iam.dto.RegisterRequest;
import com.aula.microservices.iam.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login
     * Cuerpo: { "email": "...", "password": "..." }
     * Devuelve: token JWT + datos del usuario
     * Uso desde Android/Web: guardar el token y enviarlo en cada petición
     */
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return new ApiResponse<>(true, "Login correcto", authService.login(request));
    }

    /**
     * POST /api/auth/register
     * Cuerpo: { "fullName": "...", "email": "...", "password": "...", "role": "ALUMNO" }
     * Roles disponibles: ALUMNO, PROFESOR, JEFATURA, SECRETARIA, DIRECCION, ADMIN
     */
    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ApiResponse<>(true, "Usuario registrado correctamente", authService.register(request));
    }

    /**
     * GET /api/auth/me
     * Requiere token JWT en el header Authorization: Bearer <token>
     * El gateway valida el token y nos pasa el email y rol como headers internos
     */
    @GetMapping("/me")
    public ApiResponse<String> me(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {
        return new ApiResponse<>(true, "Usuario autenticado", email + " [" + role + "]");
    }
}