package com.example.rx.models;

public class Message {

    private final String phoneNumber;

    private final String messageBody;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public Message(String phoneNumber, String messageBody) {
        this.phoneNumber = phoneNumber;
        this.messageBody = messageBody;
    }

    @Override
    public String toString() {
        return phoneNumber + " : " + messageBody;
    }
}
