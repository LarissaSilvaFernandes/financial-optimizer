package com.hackathon.financialoptimizer.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Financial Optimizer API")
                        .description("""
                                Motor de Otimização de Decisões Financeiras — Hackathon ADA 2026.

                                Utiliza o algoritmo da mochila (Knapsack DP) para recomendar a melhor
                                combinação de gastos a cortar e investimentos a priorizar, dentro de
                                uma restrição de orçamento. As decisões são explicadas em linguagem
                                natural via LLM (Spring AI + RAG).
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hackathon ADA 2026 — Artemisia Elas+ Tech")
                        )
                );
    }
}
