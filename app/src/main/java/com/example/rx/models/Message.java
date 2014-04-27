package com.example.rx.models;

import lombok.Data;

@Data
public class Message {

    private final String phoneNumber;

    private final String messageBody;

    public Message(String phoneNumber, String messageBody) {
        this.phoneNumber = phoneNumber;
        this.messageBody = messageBody;
    }

    @Override
    public String toString() {
        return phoneNumber + " : " + messageBody;
    }
}
