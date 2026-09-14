package com.gowtham.localaiagent.Controller;

import com.gowtham.localaiagent.Service.OllamaService;
import com.gowtham.localaiagent.Service.ToolDispatcher;
import com.gowtham.localaiagent.dto.AgentNextAction;
import com.gowtham.localaiagent.dto.AgentPlan;
import com.gowtham.localaiagent.dto.ChatRequest;
import com.gowtham.localaiagent.dto.ToolDecision;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final OllamaService ollamaService;
    private final ToolDispatcher toolDispatcher;

    public ChatController(
            OllamaService ollamaService,
            ToolDispatcher toolDispatcher) {
        this.ollamaService = ollamaService;
        this.toolDispatcher = toolDispatcher;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {

        String userMessage = request.getMessage();
        String toolResult = "";

        int loopCount = 0;

        while (true) {
            loopCount++;
            // Safety limit so the agent cannot run forever.
            if (loopCount > 5) {
                return "Agent stopped: maximum number of steps reached.";
            }
            System.out.println("========== AGENT STEP " + loopCount + " ==========");

            AgentNextAction action =
                    ollamaService.getNextAction(
                            userMessage,
                            toolResult
                    );

            System.out.println("Type: " + action.getType());
            System.out.println("Tool: " + action.getTool());

            if ("final".equals(action.getType())) {
                return action.getMessage();
            }

            toolResult =
                    toolDispatcher.execute(action);

            System.out.println("Tool result: " + toolResult);
        }
    }
}
