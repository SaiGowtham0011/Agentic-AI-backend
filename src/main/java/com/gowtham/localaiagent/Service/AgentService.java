package com.gowtham.localaiagent.Service;

import com.gowtham.localaiagent.dto.AgentInput;
import com.gowtham.localaiagent.dto.AgentNextAction;
import com.gowtham.localaiagent.dto.AgentState;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class AgentService {
    private final OllamaService ollamaService;
    private final ToolDispatcher toolDispatcher;
    private final ObjectMapper objectMapper;

    public AgentService(
            OllamaService ollamaService,
            ToolDispatcher toolDispatcher,
            ObjectMapper objectMapper) {
        this.ollamaService = ollamaService;
        this.toolDispatcher = toolDispatcher;
        this.objectMapper = objectMapper;
    }
    /**
     * Runs the complete agent loop for one user request.
     */
    public String run(String userMessage) {

        // Create the agent's memory for this request
        AgentState state = new AgentState(userMessage);

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

            // Convert the selected action into JSON.
            //
            // This allows AgentInput to store the complete
            // input given to the tool without knowing
            // which parameters that particular tool needs.
            String input;

            try {

                input = objectMapper.writeValueAsString(action);

            } catch (Exception e) {

                return "Failed to record agent input: "
                        + e.getMessage();
            }

            // Execute the selected Java tool
            String result =
                    toolDispatcher.execute(action);

            // Store the complete tool execution as one object.
            AgentInput agentInput =
                    new AgentInput(
                            action.getTool(),
                            input,
                            result
                    );

            // Add the execution to the agent's memory.
            state.addAgentInput(agentInput);

            System.out.println(
                    "Tool result: " + result
            );
        }
    }
}
