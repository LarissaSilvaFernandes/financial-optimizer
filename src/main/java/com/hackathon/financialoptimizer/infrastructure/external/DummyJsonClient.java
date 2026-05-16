package com.hackathon.financialoptimizer.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummyJsonClient {

    private final ObjectMapper objectMapper;

    @Value("${app.external.dummyjson-url}")
    private String baseUrl;

    public record ExternalProduct(String title, BigDecimal price, String category) {}

    public List<ExternalProduct> fetchProductsByCategories(List<String> categories) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<List<ExternalProduct>>> futures = categories.stream()
                    .map(cat -> executor.submit(() -> fetchByCategory(cat)))
                    .toList();

            List<ExternalProduct> results = new ArrayList<>();
            for (Future<List<ExternalProduct>> future : futures) {
                results.addAll(future.get());
            }
            return results;
        } catch (Exception e) {
            log.error("Erro na ingestão concurrent do DummyJson: {}", e.getMessage());
            return List.of();
        }
    }

    private List<ExternalProduct> fetchByCategory(String category) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/products/category/" + category + "?limit=5"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode products = root.get("products");

            List<ExternalProduct> result = new ArrayList<>();
            if (products != null && products.isArray()) {
                for (JsonNode p : products) {
                    result.add(new ExternalProduct(
                            p.get("title").asText(),
                            BigDecimal.valueOf(p.get("price").asDouble()),
                            category
                    ));
                }
            }
            log.info("Categoria '{}': {} produtos importados via virtual thread", category, result.size());
            return result;

        } catch (Exception e) {
            log.warn("Falha ao buscar categoria '{}': {}", category, e.getMessage());
            return List.of();
        }
    }
}
