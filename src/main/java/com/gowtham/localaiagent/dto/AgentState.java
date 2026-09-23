package com.gowtham.localaiagent.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the information collected while the agent is working.
 *
 * The agent can perform multiple tool executions.
 * This class remembers those executions so the LLM
 * can understand what has already happened.
 */
public class AgentState {

    // The original request from the user
    private String userMessage;

    // Stores complete tool executions performed by the agent
    private List<AgentInput> agentInputs = new ArrayList<>();

    public AgentState(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public List<AgentInput> getAgentInputs() {
        return agentInputs;
    }

    /**
     * Records one complete tool execution.
     */
    public void addAgentInput(AgentInput agentInput) {
        agentInputs.add(agentInput);
    }
}