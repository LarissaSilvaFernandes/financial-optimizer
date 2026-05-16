package com.hackathon.financialoptimizer.presentation.controller;

import com.hackathon.financialoptimizer.application.usecase.investment.*;
import com.hackathon.financialoptimizer.presentation.dto.request.InvestmentRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.InvestmentResponse;
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
@RequestMapping("/api/investments")
@RequiredArgsConstructor
@Tag(name = "Investimentos")
@SecurityRequirement(name = "bearerAuth")
public class InvestmentController {

    private final RegisterInvestmentUseCase registerUseCase;
    private final ListInvestmentsUseCase listUseCase;
    private final DeleteInvestmentUseCase deleteUseCase;

    @PostMapping
    @Operation(summary = "Registrar uma opção de investimento")
    public ResponseEntity<InvestmentResponse> register(
            @Valid @RequestBody InvestmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUseCase.execute(request));
    }

    @GetMapping
    @Operation(summary = "Listar investimentos do usuário")
    public ResponseEntity<List<InvestmentResponse>> list() {
        return ResponseEntity.ok(listUseCase.execute());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um investimento")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
