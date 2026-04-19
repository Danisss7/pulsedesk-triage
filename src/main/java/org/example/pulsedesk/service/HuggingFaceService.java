package org.example.pulsedesk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pulsedesk.dto.TriageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    @Value("${huggingface.api.model}")
    private String model;

    @Value("${huggingface.api.token}")
    private String apiToken;

    public HuggingFaceService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public TriageResult analyzeComment(String commentText) {
        if (apiToken == null || apiToken.isBlank()) {
            throw new RuntimeException("HF_API_TOKEN is missing");
        }

        String prompt = """
                You are a support triage assistant for PulseDesk.

                Analyze the following user comment and decide if it should become a support ticket.

                Rules:
                - Create a ticket only for actionable issues, complaints, requests, billing problems, account issues, or bugs.
                - Do not create a ticket for compliments or non-actionable praise.
                - Allowed categories: bug, feature, billing, account, other
                - Allowed priorities: low, medium, high
                - Return ONLY valid JSON with this exact structure:
                {
                  "shouldCreateTicket": true,
                  "title": "short ticket title",
                  "category": "bug",
                  "priority": "high",
                  "summary": "short 1-2 sentence summary"
                }

                Comment:
                "%s"
                """.formatted(commentText);

        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(userMessage));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to get response from Hugging Face: " + response.getStatusCode());
        }

        return parseChatResponse(response.getBody());
    }

    private TriageResult parseChatResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode choices = root.path("choices");
            if (!choices.isArray() || choices.isEmpty()) {
                throw new RuntimeException("No choices in model response: " + responseBody);
            }

            String content = choices.get(0).path("message").path("content").asText();

            String json = extractJson(content);
            JsonNode resultNode = objectMapper.readTree(json);

            TriageResult result = new TriageResult();
            result.setShouldCreateTicket(resultNode.path("shouldCreateTicket").asBoolean(false));
            result.setTitle(resultNode.path("title").asText(""));
            result.setCategory(normalizeCategory(resultNode.path("category").asText("other")));
            result.setPriority(normalizePriority(resultNode.path("priority").asText("low")));
            result.setSummary(resultNode.path("summary").asText(""));

            if (!result.isShouldCreateTicket()) {
                result.setTitle("");
                result.setCategory("other");
                result.setPriority("low");
                result.setSummary("Comment does not require a support ticket.");
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage(), e);
        }
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start < 0 || end < 0 || end <= start) {
            throw new RuntimeException("No JSON found in model output: " + text);
        }
        return text.substring(start, end + 1);
    }

    private String normalizeCategory(String category) {
        String value = category == null ? "other" : category.trim().toLowerCase();
        return switch (value) {
            case "bug", "feature", "billing", "account", "other" -> value;
            default -> "other";
        };
    }

    private String normalizePriority(String priority) {
        String value = priority == null ? "low" : priority.trim().toLowerCase();
        return switch (value) {
            case "low", "medium", "high" -> value;
            default -> "low";
        };
    }
}