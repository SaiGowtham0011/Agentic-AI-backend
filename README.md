# AI Agent Backend

A fundamental AI agent backend built with **Java, Spring Boot, Ollama, and Qwen3**.

This project was built to understand how an AI agent works internally — how an LLM can decide what action to take, execute external tools, observe the results, maintain state, and continue working until it can produce a final answer.

The project uses a **locally running Qwen3 4B model through Ollama**, so the core agent reasoning happens locally.

---

## 🚀 Project Overview

A normal LLM application generally follows:

```text
User
 ↓
LLM
 ↓
Response
```

An AI agent works differently:

```text
User
 ↓
LLM
 ↓
Decide next action
 ↓
Execute tool
 ↓
Observe result
 ↓
Update agent state
 ↓
LLM
 ↓
Decide next action
 ↓
...
 ↓
Final response
```

This project implements that fundamental agent loop using Spring Boot.

---

## 🧠 What This Project Demonstrates

- Local LLM integration using Ollama
- Qwen3 4B model integration
- AI agent loop
- Multi-step tool execution
- Agent state management
- Tool abstraction using Java interfaces
- Tool registry
- Tool dispatcher
- LLM-generated structured actions
- Spring Boot dependency injection
- Basic agent execution safety limits

---

## 🏗️ Architecture

```text
                         ┌─────────────────┐
                         │     Qwen3       │
                         │   Local LLM     │
                         └────────┬────────┘
                                  │
                           Next action
                                  │
                                  ▼
┌──────────────┐         ┌─────────────────┐
│ ChatRequest  │ ──────► │  AgentService   │
└──────────────┘         └────────┬────────┘
                                  │
                           Agent State
                                  │
                                  ▼
                         ┌─────────────────┐
                         │ ToolDispatcher  │
                         └────────┬────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │  ToolRegistry   │
                         └────────┬────────┘
                                  │
                         ┌────────┴────────┐
                         ▼                 ▼
                CalculatorTools        Timetool
```

---

## 🔄 Agent Execution Flow

For example, when the user asks:

```text
What time is it and what is 25 multiplied by 40?
```

The agent can perform multiple steps.

### Step 1 — LLM decides

```json
{
  "type": "tool",
  "tool": "current_time"
}
```

### Step 2 — Java tool executes

```text
TimeTool
    ↓
Current time
```

The result is stored in the agent state.

### Step 3 — LLM decides again

```json
{
  "type": "tool",
  "tool": "multiply",
  "a": 25,
  "b": 40
}
```

### Step 4 — Java tool executes

```text
CalculatorTools
    ↓
1000
```

### Step 5 — LLM produces the final response

```text
The current time is ... and 25 multiplied by 40 is 1000.
```

This demonstrates the fundamental:

```text
Think → Act → Observe → Think → Act → Observe → Final
```

agent pattern.

---

# 🧩 Core Components

## AgentService

`AgentService` contains the main agent loop.

Responsibilities:

- Create agent state
- Ask the LLM for the next action
- Execute tools
- Store tool results
- Continue the loop
- Return the final response
- Prevent unlimited execution using a step limit

---

## AgentState

`AgentState` stores information collected during the current agent run.

It contains:

```text
User request
+
Previous tool executions
+
Tool inputs
+
Tool results
```

This allows the LLM to see what has already happened before deciding its next action.

---

## AgentInput

`AgentInput` represents one complete tool execution.

It stores:

```text
Tool
Input
Result
```

Example:

```text
Tool: multiply
Input: {"type":"tool","tool":"multiply","a":25,"b":40}
Result: 1000.0
```

---

## AgentTool

`AgentTool` is the common interface used by executable agent tools.

```java
@FunctionalInterface
public interface AgentTool {

    String execute(AgentNextAction action);
}
```

It provides a common contract so different tools can be registered and executed through the same mechanism.

---

## ToolRegistry

`ToolRegistry` keeps track of the tools available to the agent.

Current tools include:

```text
add
subtract
multiply
divide
current_time
```

The registry maps the tool name selected by the LLM to the corresponding executable tool.

---

## ToolDispatcher

`ToolDispatcher` receives the tool requested by the LLM and executes the corresponding registered tool.

Its responsibility is:

```text
Tool name
   ↓
ToolRegistry
   ↓
AgentTool
   ↓
Execution result
```

---

## Available Tools

### CalculatorTools

Provides:

```text
add(a, b)
subtract(a, b)
multiply(a, b)
divide(a, b)
```

### Timetool

Provides:

```text
current_time()
```

---

# 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming language |
| Spring Boot | Backend framework |
| Ollama | Local LLM runtime |
| Qwen3 4B | Local language model |
| Maven | Dependency management and build |
| Jackson | JSON processing |
| Postman | API testing |

---

# 📁 Project Structure

```text
src
└── main
    └── java
        └── com.gowtham.localaiagent
            │
            ├── Controller
            │   └── ChatController.java
            │
            ├── Service
            │   ├── AgentService.java
            │   ├── OllamaService.java
            │   ├── ToolDispatcher.java
            │   └── ToolRegistry.java
            │
            ├── Interfaces
            │   └── AgentTool.java
            │
            ├── Tools
            │   ├── CalculatorTools.java
            │   └── Timetool.java
            │
            └── dto
                ├── AgentInput.java
                ├── AgentNextAction.java
                ├── AgentState.java
                ├── ChatRequest.java
                ├── OllamaRequest.java
                └── OllamaResponse.java
```

---

# ⚙️ Setup

## 1. Requirements

Install:

- Java 21
- Maven
- Ollama
- Qwen3 4B

---

## 2. Download Qwen3

Pull the model using Ollama:

```bash
ollama pull qwen3:4b
```

Verify that the model is available:

```bash
ollama list
```

---

## 3. Start Ollama

Run:

```bash
ollama serve
```

Ollama runs locally at:

```text
http://localhost:11434
```

---

## 4. Start the Spring Boot application

From the project directory:

```bash
./mvnw spring-boot:run
```

Or run the Spring Boot application directly from IntelliJ IDEA.

---

# 🧪 API Usage

## Endpoint

```http
POST /api/chat
```

### Example Request

```json
{
  "message": "What is 25 multiplied by 40?"
}
```

### Example Response

```text
1000
```

---

## Multi-Step Example

### Request

```json
{
  "message": "What time is it and what is 25 multiplied by 40?"
}
```

### Example Agent Execution

```text
========== AGENT STEP 1 ==========

Tool: current_time

Tool result:
2026-09-26T17:24:29
```

Then:

```text
========== AGENT STEP 2 ==========

Tool: multiply

Tool result:
1000.0
```

Finally:

```text
========== AGENT STEP 3 ==========

Final response:
The current time is 2026-09-26T17:24:29
and 25 multiplied by 40 is 1000.
```

---

# 🔐 Safety

The agent has a maximum execution limit of **5 steps per request**.

This prevents an unexpected model response from causing an unlimited tool-execution loop.

The project also handles unknown tools and tool execution errors through the dispatcher.

---

# 🎯 Learning Goals

This project was built primarily to understand the fundamentals of AI agents rather than to create a production-ready enterprise framework.

The main concepts explored are:

1. How an LLM can select actions
2. How Java code can act as tools for an LLM
3. How tools can be registered
4. How an agent can execute multiple steps
5. How previous tool results can be stored
6. How the LLM can use previous results to decide its next action
7. How a Spring Boot application can integrate with a locally running LLM

---

# 🔮 Future Work

This project provides the foundation for a separate real-world AI application:

**AI Email Agent**

The planned application will apply the agent concepts learned here to email workflows such as:

```text
Gmail
  ↓
Read emails
  ↓
AI classification
  ↓
Important / Normal / Spam
  ↓
Take appropriate action
  ↓
Notifications / Reminders
```

The email agent will be developed as a **separate project** so that this repository remains focused on understanding the fundamental AI-agent architecture.

---

# 👨‍💻 Author

**Sai Gowtham**

Built as a learning and portfolio project to understand the fundamentals of agentic AI, local LLMs, tool calling, and Spring Boot integration.
