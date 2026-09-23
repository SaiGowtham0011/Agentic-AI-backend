package com.gowtham.localaiagent.Controller;

import com.gowtham.localaiagent.Service.OllamaService;
import com.gowtham.localaiagent.Service.ToolDispatcher;
import com.gowtham.localaiagent.dto.*;
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

        // Create the agent's memory for this request
        AgentState state = new AgentState(request.getMessage());

        int loopCount = 0;

        while (true) {
            loopCount++;
            // Safety limit so the agent cannot run forever.
            if (loopCount > 5) {
                return "Agent stopped: maximum number of steps reached.";
            }
            System.out.println("========== AGENT STEP " + loopCount + " ==========");

            AgentNextAction action =
                    ollamaService.getNextAction(state);

            System.out.println("Type: " + action.getType());
            System.out.println("Tool: " + action.getTool());

            // If the agent has finished, return the final answer
            if ("final".equals(action.getType())) {
                return action.getMessage();
            }

            // The agent is expected to either return "final" or "tool".
            // Increases robustness of the agent.
            if (!"tool".equals(action.getType())) {
                return "Agent returned an invalid action type: "
                        + action.getType();
            }

            // Remember which tool the agent selected
            state.addAction(action.getTool());

            // Execute the selected Java tool
            String result =
                    toolDispatcher.execute(action);

            // Remember the tool result
            state.addToolResult(result);

            System.out.println(
                    "Tool result: " + result
            );
        }
    }
}
