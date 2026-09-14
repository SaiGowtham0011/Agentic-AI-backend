package com.gowtham.localaiagent.dto;
/**
 * Represents the next action that the AI agent wants to perform.
 * The LLM can return two types of actions:
 * 1. "tool"  -> The agent wants Java to execute a tool.
 * 2. "final" -> The agent has enough information and wants to finish.
 * Example tool action:
 * {
 *   "type": "tool",
 *   "tool": "multiply",
 *   "a": 25,
 *   "b": 40
 * }
 * Example final action:
 * {
 *   "type": "final",
 *   "message": "25 × 40 = 1000"
 * }
 */
public class AgentNextAction {
    /*
     * Tells Java what kind of action the agent wants.
     *
     * Possible values:
     *
     * "tool"  -> execute a tool
     * "final" -> stop the agent loop
     */
    private String type;

    //Name of the tool the agent wants to execute.
    private String tool;

    private double a;
    private double b;
    /*
     * Final answer from the agent.
     * This is used when:
     * type = "final"
     * Example:
     * "The current time is 3:15 PM and 25 × 40 = 1000."
     */
    private String message;

    public  String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTool() {
        return tool;
    }
    public void setTool(String tool) {
        this.tool = tool;
    }
    public double getA() {
        return a;
    }
    public void setA(double a) {
        this.a = a;
    }
    public double getB() {
        return b;
    }
    public void setB(double b) {
        this.b = b;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
