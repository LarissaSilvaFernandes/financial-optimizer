package com.hackathon.financialoptimizer.presentation.controller;

import com.hackathon.financialoptimizer.application.usecase.transaction.*;
import com.hackathon.financialoptimizer.presentation.dto.request.TransactionRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.TransactionResponse;
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
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transações")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final RegisterTransactionUseCase registerUseCase;
    private final ListTransactionsUseCase listUseCase;
    private final DeleteTransactionUseCase deleteUseCase;
    private final IngestTransactionsUseCase ingestUseCase;

    @PostMapping
    @Operation(summary = "Registrar uma transação")
    public ResponseEntity<TransactionResponse> register(
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUseCase.execute(request));
    }

    @GetMapping
    @Operation(summary = "Listar transações do usuário autenticado")
    public ResponseEntity<List<TransactionResponse>> list() {
        return ResponseEntity.ok(listUseCase.execute());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma transação")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ingest")
    @Operation(summary = "Importar dados da API externa (dummyjson) com virtual threads")
    public ResponseEntity<List<TransactionResponse>> ingest() {
        return ResponseEntity.ok(ingestUseCase.execute());
    }
}
