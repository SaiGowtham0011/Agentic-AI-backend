package com.gowtham.localaiagent.Service;

import com.gowtham.localaiagent.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Service
public class OllamaService {

    private final RestClient  restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OllamaService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:11434")
                .build();
    }
    /**
     * Sends the current conversation state to the LLM
     * and asks what the agent should do next.
     *
     * The toolResult contains the result of the previous
     * tool execution. On the first call, it can be empty.
     */
    public AgentNextAction getNextAction(
            String userMessage,
            String toolResult) {

        String prompt = """
            You are an AI agent.

            User request:
            %s

            Previous tool result:
            %s

            Available tools:

            1. add(a, b)
            2. subtract(a, b)
            3. multiply(a, b)
            4. divide(a, b)
            5. current_time()

            Decide what to do NEXT.

            If you need a tool, respond ONLY with:

            {
              "type": "tool",
              "tool": "tool_name",
              "a": number,
              "b": number
            }

            For current_time, omit a and b.

            If you have enough information to answer the user,
            respond ONLY with:

            {
              "type": "final",
              "message": "your answer"
            }
            """.formatted(userMessage, toolResult);

        OllamaRequest request =
                new OllamaRequest(
                        "qwen3:4b",
                        prompt,
                        false
                );

        OllamaResponse response = restClient.post()
                .uri("/api/generate")
                .body(request)
                .retrieve()
                .body(OllamaResponse.class);

        System.out.println(
                "OLLAMA NEXT ACTION: " +
                        response.getResponse()
        );

        try {
            return objectMapper.readValue(
                    response.getResponse(),
                    AgentNextAction.class
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse agent action: "
                            + response.getResponse(),
                    e
            );
        }
    }
}
