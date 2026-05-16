package com.hackathon.financialoptimizer.application.usecase.transaction;

import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.port.TransactionRepository;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionCategory;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionType;
import com.hackathon.financialoptimizer.infrastructure.external.DummyJsonClient;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngestTransactionsUseCase {

    private final DummyJsonClient dummyJsonClient;
    private final TransactionRepository transactionRepository;
    private final SecurityUtils securityUtils;

    // Mapeamento de categorias DummyJson → TransactionCategory
    private static final Map<String, TransactionCategory> CATEGORY_MAP = Map.of(
            "groceries",        TransactionCategory.FOOD,
            "furniture",        TransactionCategory.HOUSING,
            "tops",             TransactionCategory.OTHER,
            "womens-dresses",   TransactionCategory.ENTERTAINMENT,
            "mens-shirts",      TransactionCategory.OTHER,
            "fragrances",       TransactionCategory.ENTERTAINMENT,
            "skincare",         TransactionCategory.HEALTH,
            "home-decoration",  TransactionCategory.HOUSING,
            "laptops",          TransactionCategory.EDUCATION,
            "smartphones",      TransactionCategory.ENTERTAINMENT
    );

    public List<TransactionResponse> execute() {
        UUID userId = securityUtils.getCurrentUserId();
        List<String> categories = List.copyOf(CATEGORY_MAP.keySet());

        List<DummyJsonClient.ExternalProduct> products =
                dummyJsonClient.fetchProductsByCategories(categories);

        List<Transaction> saved = products.stream()
                .map(p -> {
                    TransactionCategory cat = CATEGORY_MAP.getOrDefault(
                            p.category(), TransactionCategory.OTHER);

                    return Transaction.create(
                            userId,
                            p.title(),
                            p.price(),
                            cat,
                            TransactionType.EXPENSE,
                            LocalDate.now(),
                            cat.isEssentialByDefault(),
                            BigDecimal.valueOf(20),
                            5,
                            "EXTERNAL_API"
                    );
                })
                .map(transactionRepository::save)
                .toList();

        log.info("Ingestão concluída: {} transações importadas para userId={}", saved.size(), userId);
        return saved.stream().map(RegisterTransactionUseCase::toResponse).toList();
    }
}
