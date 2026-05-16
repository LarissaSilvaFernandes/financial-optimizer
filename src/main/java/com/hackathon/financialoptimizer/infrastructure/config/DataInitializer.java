package com.hackathon.financialoptimizer.infrastructure.config;

import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.OptimizationRuleJpaEntity;
import com.hackathon.financialoptimizer.infrastructure.persistence.repository.OptimizationRuleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final OptimizationRuleJpaRepository ruleRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (ruleRepository.count() > 0) return;

        List<OptimizationRuleJpaEntity> rules = List.of(
                rule("Cortar gastos com entretenimento não-essenciais",
                        "Gastos com entretenimento acima de 15% da renda mensal devem ser reduzidos " +
                        "em pelo menos 30%, pois raramente são essenciais e têm alto potencial de corte.",
                        "EXPENSE_CUT"),
                rule("Priorizar renda fixa para perfis conservadores",
                        "Investidores com tolerância a risco LOW devem alocar pelo menos 60% em " +
                        "renda fixa (CDB, Tesouro Direto) para garantir segurança do capital.",
                        "INVESTMENT_PRIORITY"),
                rule("Fundo de emergência antes de investimentos",
                        "Antes de qualquer investimento, o usuário deve ter reserva de emergência " +
                        "equivalente a 6 meses de despesas essenciais. Priorizar poupança/CDB liquidez diária.",
                        "INVESTMENT_PRIORITY"),
                rule("Eliminar dívidas de alto custo primeiro",
                        "Dívidas com juros acima de 2% ao mês (cartão de crédito, cheque especial) " +
                        "têm prioridade máxima. O ROI de quitar essas dívidas é superior a qualquer investimento.",
                        "EXPENSE_CUT"),
                rule("Regra 50-30-20 de orçamento",
                        "Distribuição ideal: 50% para necessidades essenciais (moradia, alimentação, saúde), " +
                        "30% para desejos (entretenimento, lazer), 20% para poupança e investimentos.",
                        "RISK_FILTER"),
                rule("Diversificação para perfis moderados",
                        "Investidores MEDIUM risk devem diversificar: 40% renda fixa, 30% ações/fundos, " +
                        "20% fundos imobiliários e 10% em outros ativos para balancear risco e retorno.",
                        "INVESTMENT_PRIORITY"),
                rule("Maximização de ROI em perfis arrojados",
                        "Investidores HIGH risk podem alocar até 60% em renda variável (ações, ETFs, " +
                        "cripto) buscando retornos acima de 12% ao ano, aceitando maior volatilidade.",
                        "INVESTMENT_PRIORITY"),
                rule("Redução de gastos supérfluos com transporte",
                        "Se o gasto com transporte supera 10% da renda, avaliar alternativas: " +
                        "transporte público, carpooling ou mudança de rotina podem gerar economia de 20-40%.",
                        "EXPENSE_CUT")
        );

        ruleRepository.saveAll(rules);
        log.info("Seed: {} regras de otimização carregadas", rules.size());
    }

    private OptimizationRuleJpaEntity rule(String name, String description, String type) {
        return OptimizationRuleJpaEntity.builder()
                .name(name)
                .description(description)
                .ruleType(type)
                .active(true)
                .build();
    }
}
