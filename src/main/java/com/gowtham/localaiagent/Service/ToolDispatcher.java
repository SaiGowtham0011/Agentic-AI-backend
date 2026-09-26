package com.gowtham.localaiagent.Service;

import com.gowtham.localaiagent.Interfaces.AgentTool;
import com.gowtham.localaiagent.dto.AgentNextAction;
import org.springframework.stereotype.Service;

/**
 * Executes the tool requested by the AI agent.
 *
 * The dispatcher connects the agent's requested tool name
 * with the executable tool stored in the registry.
 */
@Service
public class ToolDispatcher {

    private final ToolRegistry toolRegistry;

    public ToolDispatcher(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    /**
     * Executes the tool selected by the LLM.
     */
    public String execute(AgentNextAction action) {

        String toolName = action.getTool();

        // Find the executable tool from the registry.
        AgentTool tool = toolRegistry.getTool(toolName);

        // The LLM may request a tool that does not exist.
        if (tool == null) {
            return "Tool not found: " + toolName;
        }

        // Execute the registered Java tool.
        try {
            return tool.execute(action);

        } catch (Exception e) {
            return "Tool execution failed: " + e.getMessage();
        }
    }
}