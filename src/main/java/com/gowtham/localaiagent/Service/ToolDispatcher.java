package com.gowtham.localaiagent.Service;

import com.gowtham.localaiagent.Tools.CalculatorTools;
import com.gowtham.localaiagent.Tools.Timetool;
import com.gowtham.localaiagent.dto.AgentNextAction;
import com.gowtham.localaiagent.dto.AgentPlan;
import com.gowtham.localaiagent.dto.ToolDecision;
import org.springframework.stereotype.Service;

@Service
public class ToolDispatcher {
    private final CalculatorTools calculatorTools;
    private final Timetool timetool;

    public ToolDispatcher(CalculatorTools calculatorTools, Timetool timetool) {
        this.calculatorTools = calculatorTools;
        this.timetool = timetool;
    }

    /**
     * Executes ONE action requested by the AI agent.
     *
     * The AI decides which tool to use.
     * Java actually executes the tool.
     */
    public String execute(AgentNextAction action) {

        // Get the tool name selected by the AI
        String tool = action.getTool();

        switch (tool) {

            case "add":
                return String.valueOf(
                        calculatorTools.add(
                                action.getA(),
                                action.getB()
                        )
                );

            case "subtract":
                return String.valueOf(
                        calculatorTools.subtract(
                                action.getA(),
                                action.getB()
                        )
                );

            case "multiply":
                return String.valueOf(
                        calculatorTools.multiply(
                                action.getA(),
                                action.getB()
                        )
                );

            case "divide":
                return String.valueOf(
                        calculatorTools.divide(
                                action.getA(),
                                action.getB()
                        )
                );

            case "current_time":
            case "current_time()":
                return timetool.getCurrentTime();

            default:
                return "Tool not found: " + tool;
        }
    }
        //return results.toString();
}
