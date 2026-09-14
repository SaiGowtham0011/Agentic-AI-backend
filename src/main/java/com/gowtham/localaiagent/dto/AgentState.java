package com.gowtham.localaiagent.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the information collected while the agent is working.
 *
 * The agent can perform multiple steps.
 * This class remembers those steps so the LLM
 * can understand what has already happened.
 */
public class AgentState {

    // The original request from the user
    private String userMessage;

    // Stores the actions performed by the agent
    private List<String> actions = new ArrayList<>();

    // Stores the results returned by the tools
    private List<String> toolResults = new ArrayList<>();

    public AgentState(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public List<String> getActions() {
        return actions;
    }

    public List<String> getToolResults() {
        return toolResults;
    }

    /**
     * Records an action performed by the agent.
     */
    public void addAction(String action) {
        actions.add(action);
    }

    /**
     * Records the result returned by a tool.
     */
    public void addToolResult(String result) {
        toolResults.add(result);
    }
}