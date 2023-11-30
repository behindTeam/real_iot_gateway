package com.front.message;

import java.util.Arrays;
import java.util.UUID;

public class MyMqttMessage extends Message {
    byte[] payload;
    UUID senderId;

    public MyMqttMessage(UUID senderId, byte[] payload) {
        this.payload = Arrays.copyOf(payload, payload.length);
        this.senderId = senderId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return new String(payload) + Arrays.toString(payload);
    }
}
