package com.gowtham.localaiagent.Interfaces;

import com.gowtham.localaiagent.dto.AgentNextAction;

/**
 * Common contract for tools that can be executed by the AI agent.
 */
@FunctionalInterface
public interface AgentTool {

    /**
     * Executes a tool using the action selected by the LLM.
     */
    String execute(AgentNextAction action);
}