package backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    private final String apiKey;
    private final String model;
    private final WebClient webClient;

    public GroqService(
            @Value("${groq.api-key:}") String apiKey,
            @Value("${groq.model:openai/gpt-oss-120b}") String model) {

        this.apiKey = apiKey;
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com")
                .build();
    }

    public String generateSummary(String prompt) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Groq API key is not configured. Please set the GROQ_API_KEY environment variable or switch app.llm.provider to 'ollama'."
            );
        }

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.3
        );

        Map<?, ?> response = webClient.post()
                .uri("/openai/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey.trim())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("choices")) {
            throw new RuntimeException("Received empty or invalid response from Groq API.");
        }

        List<?> choices = (List<?>) response.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("No choices returned from Groq API.");
        }

        Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
        Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");
        if (message == null || !message.containsKey("content")) {
            throw new RuntimeException("No message content returned from Groq API.");
        }

        return message.get("content").toString();
    }
}
