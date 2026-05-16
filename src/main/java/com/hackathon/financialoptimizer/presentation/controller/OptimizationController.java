package com.hackathon.financialoptimizer.presentation.controller;

import com.hackathon.financialoptimizer.application.usecase.optimization.GetOptimizationResultUseCase;
import com.hackathon.financialoptimizer.application.usecase.optimization.RunOptimizationUseCase;
import com.hackathon.financialoptimizer.presentation.dto.request.OptimizationRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.OptimizationResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/optimization")
@RequiredArgsConstructor
@Tag(name = "Otimização")
@SecurityRequirement(name = "bearerAuth")
public class OptimizationController {

    private final RunOptimizationUseCase runUseCase;
    private final GetOptimizationResultUseCase getResultUseCase;

    @PostMapping("/run")
    @Operation(summary = "Executar algoritmo knapsack e retornar carteira otimizada")
    public ResponseEntity<OptimizationResultResponse> run(
            @Valid @RequestBody OptimizationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(runUseCase.execute(request));
    }

    @GetMapping("/results")
    @Operation(summary = "Listar histórico de otimizações do usuário")
    public ResponseEntity<List<OptimizationResultResponse>> listResults() {
        return ResponseEntity.ok(getResultUseCase.listAll());
    }

    @GetMapping("/results/{id}")
    @Operation(summary = "Obter resultado de otimização específico")
    public ResponseEntity<OptimizationResultResponse> getResult(@PathVariable UUID id) {
        return ResponseEntity.ok(getResultUseCase.getById(id));
    }
}
