package com.hackathon.financialoptimizer.presentation.controller;

import com.hackathon.financialoptimizer.application.usecase.profile.CreateFinancialProfileUseCase;
import com.hackathon.financialoptimizer.application.usecase.profile.GetFinancialProfileUseCase;
import com.hackathon.financialoptimizer.presentation.dto.request.FinancialProfileRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.FinancialProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Perfil Financeiro")
@SecurityRequirement(name = "bearerAuth")
public class FinancialProfileController {

    private final CreateFinancialProfileUseCase createUseCase;
    private final GetFinancialProfileUseCase getUseCase;

    @PostMapping
    @Operation(summary = "Criar ou atualizar perfil financeiro")
    public ResponseEntity<FinancialProfileResponse> createOrUpdate(
            @Valid @RequestBody FinancialProfileRequest request) {
        return ResponseEntity.ok(createUseCase.execute(request));
    }

    @GetMapping
    @Operation(summary = "Obter perfil do usuário autenticado")
    public ResponseEntity<FinancialProfileResponse> get() {
        return ResponseEntity.ok(getUseCase.execute());
    }
}
