package com.gowtham.localaiagent.Service;

import com.gowtham.localaiagent.Tools.CalculatorTools;
import com.gowtham.localaiagent.Tools.Timetool;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of all tools available to the AI agent.
 *
 * The LLM gives us a tool name such as:
 *
 * "multiply"
 *
 * The registry helps us find the Java code that can execute that tool.
 */
@Service
public class ToolRegistry {

    // Stores tool names and the corresponding tool implementation
    private final Map<String,Object> tools = new HashMap<String,Object>();

    public ToolRegistry(
            CalculatorTools calculatorTools,
            Timetool timetool
    ){
        // Register calculator tools
        tools.put("add", calculatorTools);
        tools.put("subtract", calculatorTools);
        tools.put("multiply", calculatorTools);
        tools.put("divide", calculatorTools);

        // Register time tool
        tools.put("current_time", timetool);
        tools.put("current_time()", timetool);
    }

    /**
     * Finds the Java tool implementation associated with the requested tool name.
     */
    public Object getTool(String toolName){
        return tools.get(toolName);
    }
}
