package com.hackathon.financialoptimizer.infrastructure.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringAiService {

    private final ChatClient chatClient;

    public String explainOptimization(String ragContext) {
        String prompt = """
                Você é um consultor financeiro especializado em otimização de carteiras.
                Com base nas informações abaixo, explique em linguagem clara e acessível
                por que esta combinação de investimentos e cortes de gastos foi recomendada,
                destacando os principais benefícios para o perfil financeiro do usuário.

                %s

                Forneça uma explicação objetiva em até 5 parágrafos, focando nos benefícios
                concretos e na lógica por trás das escolhas do algoritmo.
                """.formatted(ragContext);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    public String answerFinancialQuestion(String question, String userContext) {
        String prompt = """
                Você é um assistente financeiro pessoal. Use o contexto abaixo sobre o
                perfil financeiro do usuário para responder à pergunta de forma personalizada.

                === CONTEXTO DO USUÁRIO ===
                %s

                === PERGUNTA ===
                %s

                Responda de forma clara, prática e direta, com base nos dados do usuário.
                """.formatted(userContext, question);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
