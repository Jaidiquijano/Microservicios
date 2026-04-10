package com.aula.microservices.iam.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aula.microservices.iam.dto.AuthResponse;
import com.aula.microservices.iam.dto.LoginRequest;
import com.aula.microservices.iam.dto.RegisterRequest;
import com.aula.microservices.iam.entity.User;
import com.aula.microservices.iam.repository.UserRepository;
import com.aula.microservices.iam.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest request) {
        // Buscar usuario por email
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas"));

        // Comprobar contraseña
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        // Comprobar que el usuario está activo
        if (!user.getActive()) {
            throw new IllegalStateException("Usuario desactivado. Contacta con administración.");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                jwtService.getExpiration()
        );
    }

    public AuthResponse register(RegisterRequest request) {
        // Comprobar que el email no existe ya
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.fullName(),
                request.role()
        );

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                jwtService.getExpiration()
        );
    }
}