<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20AI-1.0.0-6DB33F?logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-Security-000000?logo=jsonwebtokens&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?logo=swagger&logoColor=black" />
</p>

# Financial Optimizer API

## Hackathon Artemisia Elas+ Tech — Projeto Back-end | ADA 2026

---

### Sobre o Hackathon

O **Hackathon Artemisia Elas+ Tech** e o projeto de conclusao de curso da **trilha Backend** da Ada Tech 2026. O tema-mae proposto e a construcao de um **Sistema Inteligente de Analise de Perfil Financeiro, Consumo e Geracao de Insights** — uma API REST capaz de coletar, processar e analisar dados financeiros, expor informacoes de forma segura e gerar insights inteligentes com apoio de modelos de linguagem (LLM).

Cada equipe deve cobrir **6 modulos** da formacao: Java Moderno, Estruturas de Dados e Programacao Dinamica, API REST e Persistencia, Inteligencia Artificial, Qualidade de Codigo e Arquitetura/Seguranca.

### Subtema escolhido: 4.2 — Motor de Otimizacao de Decisoes Financeiras

> **Foco do Produto:** Recomendar a melhor combinacao de acoes financeiras (gastos a cortar, investimentos a priorizar) para maximizar utilidade/ROI sob restricao de orcamento.

> **Foco Tecnico:** Aplicacao de estruturas de dados e programacao dinamica (variacao do problema da mochila / ROI), camada de regras com Design Pattern Strategy, endpoint que retorna a carteira otimizada e endpoint LLM que explica em linguagem natural por que aquela combinacao foi escolhida (RAG sobre as proprias regras).

---

## Indice

- [Stack Tecnologica](#stack-tecnologica)
- [Arquitetura da Solucao](#arquitetura-da-solucao)
- [Modelo de Dados](#modelo-de-dados)
- [Algoritmo Knapsack DP](#algoritmo-knapsack-dp)
- [Design Patterns](#design-patterns)
- [Recursos Java 21](#recursos-java-21)
- [Integracao com LLM + RAG](#integracao-com-llm--rag)
- [Seguranca](#seguranca)
- [Endpoints da API](#endpoints-da-api)
- [Como Executar](#como-executar)
- [Rodando os Testes](#rodando-os-testes)
- [Docker](#docker)
- [Evidencias de Execucao (Postman)](#evidencias-de-execucao-postman)

---

## Stack Tecnologica

| Camada | Tecnologia |
|--------|------------|
| Linguagem | Java 21 (`record`, `sealed classes`, virtual threads, `switch expressions`, record patterns) |
| Framework | Spring Boot 3.3.4 |
| Seguranca | Spring Security + JWT (JJWT 0.12.6) |
| Persistencia | Spring Data JPA + Liquibase |
| Banco de Dados | H2 (desenvolvimento) / PostgreSQL 16 (producao) |
| Inteligencia Artificial | Spring AI 1.0.0 + Anthropic Claude (claude-haiku-4.5) |
| Documentacao | OpenAPI 3 / Swagger UI (springdoc 2.6.0) |
| Testes | JUnit 5 + Mockito + AssertJ |
| Build | Maven 3.9+ (wrapper incluso) |
| Deploy | Docker multi-stage + Docker Compose |
| API Externa | DummyJSON (ingestao de dados com virtual threads) |

---

## Arquitetura da Solucao

O projeto segue **Clean Architecture** com separacao rigorosa em 4 camadas:

```
src/main/java/com/hackathon/financialoptimizer/
│
├── domain/                    Camada de Dominio (sem dependencias de framework)
│   ├── entity/                  User, FinancialProfile, Transaction, Investment,
│   │                            OptimizationResult, OptimizationRule
│   ├── valueobject/             Money (record), RiskTolerance (sealed interface),
│   │                            OptimizationItem (sealed interface), TransactionCategory,
│   │                            TransactionType, InvestmentCategory, StrategyType
│   ├── port/                    Interfaces de repositorio (Ports)
│   └── exception/               DomainException, EntityNotFoundException,
│                                EmailAlreadyExistsException, UnauthorizedAccessException
│
├── application/               Camada de Aplicacao (Use Cases + Servicos)
│   ├── usecase/
│   │   ├── auth/                RegisterUserUseCase, LoginUserUseCase
│   │   ├── profile/             CreateFinancialProfileUseCase, GetFinancialProfileUseCase
│   │   ├── transaction/         RegisterTransactionUseCase, ListTransactionsUseCase,
│   │   │                        DeleteTransactionUseCase, IngestTransactionsUseCase
│   │   ├── investment/          RegisterInvestmentUseCase, ListInvestmentsUseCase,
│   │   │                        DeleteInvestmentUseCase
│   │   ├── optimization/        RunOptimizationUseCase, GetOptimizationResultUseCase
│   │   └── ai/                  ExplainOptimizationUseCase, AskFinancialQuestionUseCase
│   └── service/
│       ├── algorithm/           KnapsackOptimizer (Programacao Dinamica)
│       └── optimization/        OptimizationStrategyFactory, OptimizationContext (Builder)
│           └── strategy/        OptimizationStrategy (interface),
│                                MaximizeRoiStrategy, MinimizeRiskStrategy, BalancedStrategy
│
├── infrastructure/            Camada de Infraestrutura
│   ├── persistence/
│   │   ├── jpa/                 Entidades JPA (*JpaEntity)
│   │   ├── repository/          Spring Data JPA Repositories
│   │   └── adapter/             Adapters que implementam os Ports do dominio
│   ├── security/                SecurityConfig, JwtService, JwtAuthFilter,
│   │                            UserDetailsServiceImpl, SecurityUtils
│   ├── ai/                      SpringAiService, RagContextBuilder
│   ├── external/                DummyJsonClient (virtual threads)
│   └── config/                  DataInitializer, OpenApiConfig, ChatClientConfig
│
└── presentation/              Camada de Apresentacao (REST)
    ├── controller/              AuthController, FinancialProfileController,
    │                            TransactionController, InvestmentController,
    │                            OptimizationController, AiController
    ├── dto/
    │   ├── request/             RegisterRequest, LoginRequest, FinancialProfileRequest,
    │   │                        TransactionRequest, InvestmentRequest,
    │   │                        OptimizationRequest, AskRequest
    │   └── response/            AuthResponse, FinancialProfileResponse,
    │                            TransactionResponse, InvestmentResponse,
    │                            OptimizationResultResponse, SelectedItemResponse, AiResponse
    └── exception/               GlobalExceptionHandler (ProblemDetail RFC 7807)
```

### Fluxo de Dados

```
┌─────────────┐     ┌──────────────┐     ┌──────────────────────┐     ┌────────────┐
│  Usuario     │────>│  Controller  │────>│  Use Case            │────>│  Domain    │
│  (Postman/   │     │  (REST)      │     │  (Regras de negocio) │     │  (Entities │
│   Swagger)   │<────│              │<────│                      │<────│   + Ports) │
└─────────────┘     └──────────────┘     └──────────┬───────────┘     └────────────┘
                                                    │
                                         ┌──────────▼───────────┐
                                         │   Infrastructure     │
                                         │  ┌─────────────────┐ │
                                         │  │ JPA / PostgreSQL │ │
                                         │  │ Spring AI / LLM  │ │
                                         │  │ DummyJSON Client │ │
                                         │  │ JWT Security     │ │
                                         │  └─────────────────┘ │
                                         └──────────────────────┘
```

---

## Modelo de Dados

| Tabela | Descricao |
|--------|-----------|
| `users` | Cadastro de usuarios (email, senha BCrypt, nome) |
| `financial_profiles` | Perfil financeiro (renda, orcamento, meta de poupanca, tolerancia ao risco) |
| `transactions` | Transacoes de receita/despesa com categoria, utilidade e potencial de corte |
| `investments` | Opcoes de investimento com ROI esperado, risco e prioridade |
| `optimization_rules` | Regras de negocio financeiras usadas no RAG (8 regras pre-cadastradas) |
| `optimization_results` | Resultados das otimizacoes com itens selecionados e explicacao LLM |

Todas as tabelas possuem `user_id` como chave estrangeira, garantindo **isolamento de dados por usuario** (multi-tenant simples).

---

## Algoritmo Knapsack DP

O nucleo tecnico do sistema e o **Problema da Mochila 0/1** resolvido com **Programacao Dinamica**:

```
Entrada:  Lista de OptimizationItem (ExpenseItem | InvestmentItem) + orcamento
Saida:    Subconjunto de itens que MAXIMIZA o valor total sem exceder o orcamento

Tabela DP:  dp[i][w] = melhor valor usando os i primeiros itens com capacidade w
Backtrack:  percorre a tabela de tras pra frente para recuperar os itens selecionados
Complexidade: O(n x W)  onde W = orcamento / 10 (discretizado em blocos de R$10)
```

**Como os itens sao modelados:**

| Tipo | Peso (weight) | Valor (value) |
|------|---------------|---------------|
| `ExpenseItem` (gasto a cortar) | Economia possivel em R$ | `(10 - utilityScore) x potentialReduction / 100` |
| `InvestmentItem` (investimento) | Valor requerido em R$ | `expectedRoi x priorityScore / 10` |

**Em termos simples:** o algoritmo recebe seu orcamento e todas as opcoes disponiveis (cortar delivery, investir em CDB, cancelar streaming...). Ele testa todas as combinacoes possíveis de forma inteligente e encontra a **combinacao otima** — aquela que maximiza o retorno financeiro sem ultrapassar o limite de orcamento.

---

## Design Patterns

### Strategy Pattern (protagonista do Tema 2)

Tres estrategias intercambiaveis que preparam os itens de formas diferentes antes de enviar ao Knapsack:

| Estrategia | Classe | Comportamento |
|------------|--------|---------------|
| `MAXIMIZE_ROI` | `MaximizeRoiStrategy` | Inclui todos os investimentos e gastos nao-essenciais. Prioriza retorno maximo independente do risco. |
| `MINIMIZE_RISK` | `MinimizeRiskStrategy` | Filtra investimentos pelo perfil de risco do usuario. So inclui cortes moderados (reducao <= 50%). |
| `BALANCED` | `BalancedStrategy` | Aplica penalidade por risco (HIGH = 0.5x, MEDIUM = 0.8x, LOW = 1.0x). Equilibra retorno e seguranca. |

### Factory Pattern

`OptimizationStrategyFactory` usa `switch expression` sobre o `StrategyType` para instanciar a estrategia correta.

### Builder Pattern

`OptimizationContext.Builder` constroi o contexto de execucao do algoritmo com validacao obrigatoria no `build()`.

### Ports & Adapters

Interfaces de repositorio (`Port`) definidas no dominio, implementadas por adapters na infraestrutura — o dominio nao conhece JPA nem Spring.

---

## Recursos Java 21

| Recurso | Onde e usado |
|---------|-------------|
| `record` | `Money`, `OptimizationRequest`, `OptimizationResultResponse`, `SelectedItemResponse`, `KnapsackResult`, `ExternalProduct`, todos os DTOs |
| `sealed interface` | `RiskTolerance` (Low, Medium, High), `OptimizationItem` (ExpenseItem, InvestmentItem) |
| `switch expression` | `OptimizationStrategyFactory`, `TransactionCategory.label()`, `InvestmentCategory.label()`, `StrategyType.label()`, `BalancedStrategy` (risk penalty) |
| Record patterns | `RunOptimizationUseCase.toSelectedItem()` — `case OptimizationItem.ExpenseItem e ->` |
| Virtual threads | `DummyJsonClient.fetchProductsByCategories()` — `Executors.newVirtualThreadPerTaskExecutor()` para ingestao concorrente de multiplas categorias da API DummyJSON |
| Text blocks | Prompts do LLM em `SpringAiService`, contexto RAG em `RagContextBuilder` |
| Streams + funcional | Todos os use cases e strategies utilizam `stream().filter().map().toList()` |

---

## Integracao com LLM + RAG

### RAG — Retrieval-Augmented Generation

O sistema implementa RAG sobre os **proprios dados**:

1. **Retrieval:** `RagContextBuilder` busca no banco de dados:
   - Perfil financeiro do usuario (renda, orcamento, tolerancia ao risco)
   - Itens selecionados pelo Knapsack (nome, tipo, peso, valor, categoria, risco)
   - Regras de negocio ativas (`optimization_rules`)

2. **Augmented:** O contexto recuperado e injetado no prompt enviado ao LLM

3. **Generation:** O LLM (Claude Haiku 4.5 via Spring AI) gera a explicacao personalizada

### Regras de Negocio Pre-cadastradas (Seed)

8 regras financeiras sao inseridas automaticamente no banco na inicializacao:

| Regra | Tipo |
|-------|------|
| Cortar gastos com entretenimento nao-essenciais | `EXPENSE_CUT` |
| Priorizar renda fixa para perfis conservadores | `INVESTMENT_PRIORITY` |
| Fundo de emergencia antes de investimentos | `INVESTMENT_PRIORITY` |
| Eliminar dividas de alto custo primeiro | `EXPENSE_CUT` |
| Regra 50-30-20 de orcamento | `RISK_FILTER` |
| Diversificacao para perfis moderados | `INVESTMENT_PRIORITY` |
| Maximizacao de ROI em perfis arrojados | `INVESTMENT_PRIORITY` |
| Reducao de gastos superfluos com transporte | `EXPENSE_CUT` |

### Endpoints de IA

| Endpoint | Descricao |
|----------|-----------|
| `POST /api/ai/explain/{id}` | Explica em linguagem natural por que a combinacao foi escolhida (RAG + LLM). O resultado e salvo no banco como cache — chamadas subsequentes retornam sem chamar o LLM novamente. |
| `POST /api/ai/ask` | Q&A livre — o usuario faz perguntas sobre seus dados financeiros e a IA responde com base no perfil e transacoes recentes. |

---

## Seguranca

| Recurso | Implementacao |
|---------|---------------|
| Autenticacao | JWT (JJWT 0.12.6) — stateless, token com userId e email |
| Senha | BCrypt via `PasswordEncoder` |
| Autorizacao | Todo endpoint (exceto `/api/auth/**` e Swagger) requer `Bearer Token` |
| Isolamento de dados | Cada use case filtra por `securityUtils.getCurrentUserId()` — nenhum usuario ve dados de outro |
| Validacao de entrada | Bean Validation (`@NotBlank`, `@Email`, `@Pattern`, `@DecimalMin`, etc.) |
| Tratamento de erros | `GlobalExceptionHandler` com `ProblemDetail` (RFC 7807) |
| Endpoints publicos | Apenas `/api/auth/register`, `/api/auth/login`, Swagger UI e H2 Console (dev) |

---

## Endpoints da API

### Autenticacao

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/auth/register` | Registrar novo usuario | Nao |
| `POST` | `/api/auth/login` | Login — retorna JWT | Nao |

### Perfil Financeiro

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/profile` | Criar ou atualizar perfil financeiro | Sim |
| `GET` | `/api/profile` | Obter perfil do usuario autenticado | Sim |

### Transacoes

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/transactions` | Registrar uma transacao | Sim |
| `GET` | `/api/transactions` | Listar transacoes do usuario | Sim |
| `DELETE` | `/api/transactions/{id}` | Remover uma transacao | Sim |
| `POST` | `/api/transactions/ingest` | Importar da API externa DummyJSON (virtual threads) | Sim |

### Investimentos

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/investments` | Registrar opcao de investimento | Sim |
| `GET` | `/api/investments` | Listar investimentos do usuario | Sim |
| `DELETE` | `/api/investments/{id}` | Remover um investimento | Sim |

### Otimizacao (Knapsack DP)

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/optimization/run` | Executar algoritmo Knapsack e retornar carteira otimizada | Sim |
| `GET` | `/api/optimization/results` | Listar historico de otimizacoes | Sim |
| `GET` | `/api/optimization/results/{id}` | Obter resultado especifico | Sim |

### Inteligencia Artificial (LLM + RAG)

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/ai/explain/{optimizationId}` | Explicar resultado da otimizacao em linguagem natural (RAG + LLM) | Sim |
| `POST` | `/api/ai/ask` | Q&A financeiro livre com dados do usuario | Sim |

---

## Como Executar

### Pre-requisitos

- Java 21+
- Maven 3.9+ (ou use o wrapper `./mvnw` incluso)

### Perfil dev (H2 em arquivo local)

```bash
# Variavel necessaria apenas para os endpoints /api/ai/**
export ANTHROPIC_API_KEY=sk-ant-...

# Executar
./mvnw spring-boot:run
```

A aplicacao sobe em **http://localhost:8080**

### Swagger UI

```
http://localhost:8080/swagger-ui.html
```

### H2 Console (apenas dev)

```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/financial_optimizer_db
User: sa  |  Password: (vazio)
```

### Perfil prod (PostgreSQL)

```bash
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:postgresql://localhost:5432/financial_optimizer
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=senha
export ANTHROPIC_API_KEY=sk-ant-...

./mvnw spring-boot:run
```

---

## Rodando os Testes

```bash
./mvnw test
```

### Testes implementados

| Classe de Teste | O que testa |
|-----------------|-------------|
| `KnapsackOptimizerTest` | 7 testes unitarios do algoritmo de programacao dinamica (lista vazia, orcamento zero, todos cabem, combinacao otima conhecida, item unico, etc.) |
| `RunOptimizationUseCaseTest` | Testes do caso de uso principal com Mockito (execucao completa, perfil nao encontrado) |
| `MaximizeRoiStrategyTest` | Testes da estrategia MaximizeROI (inclui todos investimentos, filtra gastos nao-essenciais, calculo de valor) |
| `BalancedStrategyTest` | Testes da estrategia Balanced (penalidade por risco, exclusao de essenciais, combinacao gastos + investimentos) |

---

## Docker

### Docker Compose (PostgreSQL + Aplicacao)

```bash
# Subir tudo
docker-compose up --build

# Com chave da IA
ANTHROPIC_API_KEY=sk-ant-... docker-compose up --build
```

### Dockerfile (multi-stage build)

- **Stage 1 (builder):** Java 21 JDK + Maven — compila o projeto
- **Stage 2 (runtime):** Java 21 JRE Alpine — imagem leve para producao

---

## Evidencias de Execucao (Postman)

### 1. Registrar Usuario — `POST /api/auth/register`

![Registrar Usuario - Request](docs/screenshots/01-register-request.png)
![Registrar Usuario - Response 201](docs/screenshots/02-register-response-201.png)

---

### 2. Login — `POST /api/auth/login`

![Login - Request](docs/screenshots/03-login-request.png)
![Login - Response JWT](docs/screenshots/04-login-response-jwt.png)

---

### 3. Criar Perfil Financeiro — `POST /api/profile`

![Criar Perfil - Request](docs/screenshots/05-profile-create-request.png)
![Criar Perfil - Response](docs/screenshots/06-profile-create-response.png)

---

### 4. Atualizar Perfil — `POST /api/profile` (riskTolerance MEDIUM / HIGH)

![Atualizar Perfil - MEDIUM](docs/screenshots/08-profile-update-medium.png)
![Atualizar Perfil - HIGH](docs/screenshots/09-profile-update-high.png)

---

### 5. Obter Perfil Financeiro — `GET /api/profile`

![Obter Perfil - Bearer Token](docs/screenshots/07-profile-get-response.png)
![Obter Perfil - Authorization](docs/screenshots/10-profile-get-bearer.png)

---

### 6. Registrar Transacao (Despesa) — `POST /api/transactions`

![Transacao Despesa - HOUSING/EXPENSE](docs/screenshots/11-transaction-create-expense.png)

---

### 7. Registrar Transacao (Receita) — `POST /api/transactions`

![Transacao Receita - OTHER/INCOME](docs/screenshots/12-transaction-create-income.png)

---

### 8. Listar Transacoes — `GET /api/transactions`

![Listar Transacoes](docs/screenshots/13-transactions-list.png)

---

### 9. Importar Transacoes da API Externa (Virtual Threads) — `POST /api/transactions/ingest`

![Ingest DummyJSON com Virtual Threads](docs/screenshots/14-transactions-ingest-virtual-threads.png)

---

### 10. Registrar Investimento (Renda Fixa) — `POST /api/investments`

![Investimento Renda Fixa - Tesouro Selic LOW](docs/screenshots/15-investment-create-fixed-income.png)

---

### 11. Registrar Investimento (Acoes) — `POST /api/investments`

![Investimento Acoes - ETF BOVA11 HIGH](docs/screenshots/16-investment-create-stocks.png)

---

### 12. Listar Investimentos — `GET /api/investments`

![Listar Investimentos](docs/screenshots/17-investments-list.png)

---

### 13. Executar Otimizacao — MAXIMIZE_ROI — `POST /api/optimization/run`

![Otimizacao MAXIMIZE_ROI](docs/screenshots/18-optimization-run-maximize-roi.png)

---

### 14. Executar Otimizacao — BALANCED — `POST /api/optimization/run`

![Otimizacao BALANCED](docs/screenshots/19-optimization-run-balanced.png)

---

### 15. Executar Otimizacao — MINIMIZE_RISK — `POST /api/optimization/run`

![Otimizacao MINIMIZE_RISK](docs/screenshots/20-optimization-run-minimize-risk.png)

---

### 16. Listar Historico de Otimizacoes — `GET /api/optimization/results`

![Historico de Otimizacoes](docs/screenshots/21-optimization-results-list.png)

---

### 17. Obter Resultado Especifico — `GET /api/optimization/results/{id}`

![Resultado Especifico por ID](docs/screenshots/22-optimization-result-by-id.png)

---

### 18. Explicar Otimizacao via LLM + RAG — `POST /api/ai/explain/{id}`

![Explicacao RAG + LLM](docs/screenshots/23-ai-explain-rag.png)

---

### 19. Q&A Financeiro Livre — `POST /api/ai/ask`

![Q&A Livre com LLM](docs/screenshots/24-ai-ask-question.png)

---

### 20. Deletar Transacao — `DELETE /api/transactions/{id}`

![Deletar Transacao - 204 No Content](docs/screenshots/25-delete-transaction.png)

---

### 21. Deletar Investimento — `DELETE /api/investments/{id}`

![Deletar Investimento - 204 No Content](docs/screenshots/26-delete-investment.png)

---

### 22. Swagger UI — Visao Geral dos Endpoints

![Swagger UI Overview](docs/screenshots/27-swagger-ui-overview.png)

---

### 23. Cobertura de Testes — IntelliJ Coverage

![Test Coverage](docs/screenshots/28-test-coverage.png)

---

### Postman — Visao Geral da Collection

![Postman Collection Overview](docs/screenshots/00-postman-collection-overview.png)

---
