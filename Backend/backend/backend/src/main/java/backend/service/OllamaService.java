package backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class OllamaService {

    private final String provider;
    private final String ollamaModel;
    private final WebClient webClient;
    private final GroqService groqService;

    public OllamaService(
            @Value("${app.llm.provider:ollama}") String provider,
            @Value("${ollama.base-url:http://localhost:11434}") String ollamaBaseUrl,
            @Value("${ollama.model:gemma3}") String ollamaModel,
            GroqService groqService) {

        this.provider = provider;
        this.ollamaModel = ollamaModel;
        this.groqService = groqService;
        this.webClient = WebClient.builder()
                .baseUrl(ollamaBaseUrl)
                .build();
    }

    public String summarize(String text, String length, String style) {

        String lengthInstruction = switch (length.toLowerCase()) {
            case "brief" ->
                    "Keep the summary very concise. Focus only on the most important information.";
            case "detailed" ->
                    "Provide a comprehensive and detailed summary covering major points, supporting details, and important information.";
            default ->
                    "Provide a balanced summary covering the main points and important details.";
        };

        String styleInstruction = switch (style.toLowerCase()) {
            case "academic" ->
                    "Use a formal academic tone. Clearly explain concepts and preserve important arguments, findings, and conclusions.";

            case "technical" ->
                    "Focus on technical concepts, technologies, methodologies, system components, processes, and important specifications.";

            case "resume" ->
                    "Focus on achievements, skills, responsibilities, technologies, results, and important experience that could be useful for professional or resume purposes.";

            default ->
                    "Use a clear, professional, and easy-to-understand general style.";
        };

        String prompt = """
                You are a professional document summarization assistant.

                Summary Length:
                %s

                Summary Style:
                %s

                Follow this exact format:

                ## Overview
                Give a clear overview of the document.

                ## Key Points
                - List the most important points.
                - Use clear bullet points.
                - Do not repeat information.

                Important Details
                - Mention important facts, dates, names, numbers, requirements, findings, or decisions.
                - Include only information actually present in the document.

                Conclusion
                Give the main takeaway from the document.

                Rules:
                - Do not write one large paragraph.
                - Use headings and bullet points wherever appropriate.
                - Do not invent information.
                - Adapt the summary according to the selected length and style.
                - Return only the structured summary.

                Document:
                """.formatted(lengthInstruction, styleInstruction) + text;

        if ("groq".equalsIgnoreCase(provider)) {
            return groqService.generateSummary(prompt);
        }

        if ("ollama".equalsIgnoreCase(provider) || provider == null || provider.isBlank()) {
            Map<String, Object> request = Map.of(
                    "model", ollamaModel,
                    "prompt", prompt,
                    "stream", false,
                    "options", Map.of(
                            "num_predict", 500
                    )
            );

            Map<?, ?> response = webClient.post()
                    .uri("/api/generate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("response")) {
                throw new RuntimeException("Empty response from Ollama.");
            }

            return response.get("response").toString();
        }

        throw new IllegalArgumentException(
                "Unsupported LLM provider: '" + provider + "'. Supported providers are 'ollama' and 'groq'."
        );
    }
}