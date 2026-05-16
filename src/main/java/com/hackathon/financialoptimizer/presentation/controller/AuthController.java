package com.hackathon.financialoptimizer.presentation.controller;

import com.hackathon.financialoptimizer.application.usecase.auth.LoginUserUseCase;
import com.hackathon.financialoptimizer.application.usecase.auth.RegisterUserUseCase;
import com.hackathon.financialoptimizer.presentation.dto.request.LoginRequest;
import com.hackathon.financialoptimizer.presentation.dto.request.RegisterRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Registro e login de usuários")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registerUserUseCase.execute(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login — retorna JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUserUseCase.execute(request));
    }
}
