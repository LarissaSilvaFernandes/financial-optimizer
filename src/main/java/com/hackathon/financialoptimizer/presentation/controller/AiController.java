package com.hackathon.financialoptimizer.presentation.controller;

import com.hackathon.financialoptimizer.application.usecase.ai.AskFinancialQuestionUseCase;
import com.hackathon.financialoptimizer.application.usecase.ai.ExplainOptimizationUseCase;
import com.hackathon.financialoptimizer.presentation.dto.request.AskRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.AiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "IA / LLM")
@SecurityRequirement(name = "bearerAuth")
public class AiController {

    private final ExplainOptimizationUseCase explainUseCase;
    private final AskFinancialQuestionUseCase askUseCase;

    @PostMapping("/explain/{optimizationId}")
    @Operation(summary = "Explicar em linguagem natural o resultado da otimização (RAG + LLM)")
    public ResponseEntity<AiResponse> explain(@PathVariable UUID optimizationId) {
        return ResponseEntity.ok(new AiResponse(explainUseCase.execute(optimizationId)));
    }

    @PostMapping("/ask")
    @Operation(summary = "Q&A livre sobre dados financeiros do usuário")
    public ResponseEntity<AiResponse> ask(@Valid @RequestBody AskRequest request) {
        return ResponseEntity.ok(new AiResponse(askUseCase.execute(request.question())));
    }
}
