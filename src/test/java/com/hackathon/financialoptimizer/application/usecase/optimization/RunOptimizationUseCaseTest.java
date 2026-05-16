package com.hackathon.financialoptimizer.application.usecase.optimization;

import com.hackathon.financialoptimizer.application.service.algorithm.KnapsackOptimizer;
import com.hackathon.financialoptimizer.application.service.optimization.OptimizationStrategyFactory;
import com.hackathon.financialoptimizer.application.service.optimization.strategy.OptimizationStrategy;
import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.entity.OptimizationResult;
import com.hackathon.financialoptimizer.domain.port.*;
import com.hackathon.financialoptimizer.domain.valueobject.*;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.request.OptimizationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunOptimizationUseCaseTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private InvestmentRepository investmentRepository;
    @Mock private FinancialProfileRepository financialProfileRepository;
    @Mock private OptimizationResultRepository optimizationResultRepository;
    @Mock private OptimizationRuleRepository optimizationRuleRepository;
    @Mock private OptimizationStrategyFactory strategyFactory;
    @Mock private KnapsackOptimizer knapsackOptimizer;
    @Mock private SecurityUtils securityUtils;
    @Mock private OptimizationStrategy strategy;

    @InjectMocks
    private RunOptimizationUseCase useCase;

    private UUID userId;
    private FinancialProfile profile;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        profile = new FinancialProfile(
                UUID.randomUUID(), userId,
                new BigDecimal("5000"), new BigDecimal("2000"),
                new BigDecimal("500"), new RiskTolerance.Medium(),
                LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void shouldExecuteOptimizationSuccessfully() {
        var request = new OptimizationRequest("MAXIMIZE_ROI", new BigDecimal("1000"));
        var knapsackResult = new KnapsackOptimizer.KnapsackResult(List.of(), 15.0, BigDecimal.ZERO);

        when(securityUtils.getCurrentUserId()).thenReturn(userId);
        when(financialProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(transactionRepository.findByUserId(userId)).thenReturn(List.of());
        when(investmentRepository.findByUserId(userId)).thenReturn(List.of());
        when(strategyFactory.create(StrategyType.MAXIMIZE_ROI)).thenReturn(strategy);
        when(strategy.prepareItems(any(), any(), any())).thenReturn(List.of());
        when(knapsackOptimizer.optimize(any(), any())).thenReturn(knapsackResult);
        when(optimizationRuleRepository.findAllActive()).thenReturn(List.of());
        when(optimizationResultRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var response = useCase.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.strategyType()).isEqualTo("MAXIMIZE_ROI");
        assertThat(response.budgetConstraint()).isEqualByComparingTo(new BigDecimal("1000"));
        assertThat(response.totalRoiAchieved()).isEqualTo(15.0);
        verify(optimizationResultRepository).save(any(OptimizationResult.class));
    }

    @Test
    void shouldThrowWhenProfileNotFound() {
        var request = new OptimizationRequest("BALANCED", new BigDecimal("500"));

        when(securityUtils.getCurrentUserId()).thenReturn(userId);
        when(financialProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException.class,
                () -> useCase.execute(request)
        );
    }
}
