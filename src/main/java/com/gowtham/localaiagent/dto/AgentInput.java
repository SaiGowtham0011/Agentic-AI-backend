package com.gowtham.localaiagent.dto;

/**
 * Stores the information related to one tool execution
 * during the current agent run.
 *
 * It records:
 * - which tool was used
 * - what input was given to the tool
 * - what result the tool returned
 */
public class AgentInput {

    private final String tool;
    private final String input;
    private final String result;

    public AgentInput(
            String tool,
            String input,
            String result
    ) {
        this.tool = tool;
        this.input = input;
        this.result = result;
    }

    public String getTool() {
        return tool;
    }

    public String getInput() {
        return input;
    }

    public String getResult() {
        return result;
    }
}