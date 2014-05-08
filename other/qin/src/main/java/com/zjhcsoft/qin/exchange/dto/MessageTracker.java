package com.zjhcsoft.qin.exchange.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class MessageTracker {
    private Map<String, String> messages = new LinkedHashMap<>();

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }
}
