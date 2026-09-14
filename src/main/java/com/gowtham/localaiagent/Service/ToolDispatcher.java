package com.gowtham.localaiagent.Service;

import com.gowtham.localaiagent.Tools.CalculatorTools;
import com.gowtham.localaiagent.Tools.Timetool;
import com.gowtham.localaiagent.dto.AgentNextAction;
import org.springframework.stereotype.Service;

/**
 * Executes the tool requested by the AI agent.
 *
 * ToolRegistry tells us WHICH Java tool is available.
 * ToolDispatcher decides HOW to execute it.
 */
@Service
public class ToolDispatcher {

    private final ToolRegistry toolRegistry;

    private final CalculatorTools calculatorTools;
    private final Timetool timetool;

    public ToolDispatcher(
            ToolRegistry toolRegistry,
            CalculatorTools calculatorTools,
            Timetool timetool
    ) {
        this.toolRegistry = toolRegistry;
        this.calculatorTools = calculatorTools;
        this.timetool = timetool;
    }

    /**
     * Executes the action selected by the LLM.
     */
    public String execute(AgentNextAction action) {

        String tool = action.getTool();

        // Check whether the requested tool exists
        Object toolObject = toolRegistry.getTool(tool);

        if (toolObject == null) {
            return "Tool not found: " + tool;
        }

        /*
         * The registry tells us which tool class owns the tool.
         * We still need to call the appropriate Java method.
         */
        if (toolObject == calculatorTools) {

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

                default:
                    return "Unknown calculator tool: " + tool;
            }
        }

        if (toolObject == timetool) {

            switch (tool) {

                case "current_time":
                case "current_time()":
                    return timetool.getCurrentTime();

                default:
                    return "Unknown time tool: " + tool;
            }
        }

        return "Tool execution not supported: " + tool;
    }
}