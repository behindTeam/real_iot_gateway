package com.front.message;

import java.util.Arrays;
import java.util.UUID;

/**
 * {@code MyMqttMessage}는 MQTT 메시지를 나타내는 클래스입니다.
 * 
 * <p>
 * MQTT 메시지는 토픽, 발신자의 UUID, payload로 구성됩니다.
 * </p>
 * 
 * 
 * <p>
 * 토픽, 발신자 UUID, payload 등의 정보를 조회할 수 있는 메서드를 제공합니다.
 * </p>
 */
public class MyMqttMessage extends Message {
    byte[] payload;
    String topic;
    UUID senderId;

    public MyMqttMessage(UUID senderId, String topic, byte[] payload) {
        this.payload = Arrays.copyOf(payload, payload.length);
        this.topic = topic;
        this.senderId = senderId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public byte[] getPayload() {
        return payload;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return new String(payload) + Arrays.toString(payload);
    }
}
