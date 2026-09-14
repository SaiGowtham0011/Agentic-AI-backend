package com.gowtham.localaiagent.dto;

public class OllamaRequest {
    String model;
    String prompt;
    Boolean stream;

    public OllamaRequest(String model, String prompt, Boolean stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }

    public String getModel() {
        return model;
    }
    public String getPrompt() {
        return prompt;
    }
    public Boolean getStream() {
        return stream;
    }
}

