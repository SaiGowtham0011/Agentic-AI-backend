package com.gowtham.localaiagent.Service;

import com.gowtham.localaiagent.Interfaces.AgentTool;
import com.gowtham.localaiagent.Tools.CalculatorTools;
import com.gowtham.localaiagent.Tools.Timetool;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of all tools available to the AI agent.
 *
 * The registry connects the tool name returned by the LLM
 * to the Java code that can execute that tool.
 */
@Service
public class ToolRegistry {

    // Stores tool names and their executable implementations.
    private final Map<String, AgentTool> tools =
            new HashMap<>();

    public ToolRegistry(
            CalculatorTools calculatorTools,
            Timetool timetool
    ) {

        // Register calculator tools.
        tools.put(
                "add",
                action -> String.valueOf(
                        calculatorTools.add(
                                action.getA(),
                                action.getB()
                        )
                )
        );

        tools.put(
                "subtract",
                action -> String.valueOf(
                        calculatorTools.subtract(
                                action.getA(),
                                action.getB()
                        )
                )
        );

        tools.put(
                "multiply",
                action -> String.valueOf(
                        calculatorTools.multiply(
                                action.getA(),
                                action.getB()
                        )
                )
        );

        tools.put(
                "divide",
                action -> String.valueOf(
                        calculatorTools.divide(
                                action.getA(),
                                action.getB()
                        )
                )
        );

        // Register time tool.
        tools.put(
                "current_time",
                action -> timetool.getCurrentTime()
        );

        // Qwen3 sometimes returns current_time()
        // instead of current_time.
        tools.put(
                "current_time()",
                action -> timetool.getCurrentTime()
        );
    }

    /**
     * Finds the executable tool associated
     * with the requested tool name.
     */
    public AgentTool getTool(String toolName) {
        return tools.get(toolName);
    }
}