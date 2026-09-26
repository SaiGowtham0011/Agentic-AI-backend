package com.gowtham.localaiagent.Controller;

import com.gowtham.localaiagent.Service.AgentService;
import com.gowtham.localaiagent.dto.ChatRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final AgentService agentService;

    public ChatController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {

        return agentService.run(request.getMessage());
    }
}