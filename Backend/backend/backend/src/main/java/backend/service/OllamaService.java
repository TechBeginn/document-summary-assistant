package backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class OllamaService {

    private final WebClient webClient;

    public OllamaService() {
    this.webClient = WebClient.builder()
            .baseUrl("http://localhost:11434")
            .build();
    }

    public String summarize(String text) {

        String prompt = """
        You are a professional document summarization assistant.

        Analyze the document and create a clear, structured summary.

        Follow this exact format:

        ## Overview
        Give a concise 2-4 sentence overview of the document.

        ## Key Points
        - List the most important points from the document.
        - Use clear and specific bullet points.
        - Do not repeat information.

        ## Important Details
        - Mention important facts, dates, names, numbers, requirements, or decisions.
        - Include only information actually present in the document.

        ## Conclusion
        Give a brief conclusion that captures the main takeaway.

        Rules:
        - Do not write one large paragraph.
        - Use headings and bullet points.
        - Keep the summary concise but informative.
        - Do not invent information.
        - Do not mention that you are an AI.
        - Return only the summary.

        Document:
        """ + text;

        Map<String, Object> request = Map.of(
                "model", "gemma3",
                "prompt", prompt,
                "stream", false
        );

        Map response = webClient.post()
                .uri("/api/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response.get("response").toString();
    }
}