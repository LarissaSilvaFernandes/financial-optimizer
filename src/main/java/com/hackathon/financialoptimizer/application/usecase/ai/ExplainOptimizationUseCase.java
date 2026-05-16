package com.hackathon.financialoptimizer.application.usecase.ai;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.entity.OptimizationResult;
import com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException;
import com.hackathon.financialoptimizer.domain.port.FinancialProfileRepository;
import com.hackathon.financialoptimizer.domain.port.OptimizationResultRepository;
import com.hackathon.financialoptimizer.infrastructure.ai.RagContextBuilder;
import com.hackathon.financialoptimizer.infrastructure.ai.SpringAiService;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExplainOptimizationUseCase {

    private final OptimizationResultRepository optimizationResultRepository;
    private final FinancialProfileRepository financialProfileRepository;
    private final RagContextBuilder ragContextBuilder;
    private final SpringAiService springAiService;
    private final SecurityUtils securityUtils;

    @Transactional
    public String execute(UUID optimizationId) {
        UUID userId = securityUtils.getCurrentUserId();

        OptimizationResult result = optimizationResultRepository.findByIdAndUserId(optimizationId, userId)
                .orElseThrow(() -> new EntityNotFoundException("OptimizationResult", optimizationId));

        if (result.llmExplanation() != null && !result.llmExplanation().isBlank()) {
            return result.llmExplanation();
        }

        FinancialProfile profile = financialProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("FinancialProfile", userId));

        String ragContext = ragContextBuilder.build(result, profile);
        String explanation = springAiService.explainOptimization(ragContext);

        result.setLlmExplanation(explanation);
        optimizationResultRepository.save(result);

        return explanation;
    }
}
