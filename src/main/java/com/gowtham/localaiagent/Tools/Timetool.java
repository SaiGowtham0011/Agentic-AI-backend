package com.gowtham.localaiagent.Tools;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Timetool {
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }
}
