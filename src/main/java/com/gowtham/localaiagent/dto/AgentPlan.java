package com.gowtham.localaiagent.dto;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgentPlan {
    private List<ToolDecision> actions;
    public List<ToolDecision> getActions() {
        return actions;
    }
    public void setActions(List<ToolDecision> actions) {
        this.actions = actions;
    }
}
