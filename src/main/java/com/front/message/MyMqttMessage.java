package com.front.message;

import java.util.Arrays;
import java.util.UUID;

/**
 * {@code MyMqttMessage} 클래스는 MQTT 메시지를 나타내는 클래스입니다.
 * 
 * <p>
 * MQTT 메시지는 토픽, 발신자의 UUID, payload로 구성됩니다.
 * </p>
 * 
 * <p>
 * 토픽, 발신자 UUID, payload 등의 정보를 조회할 수 있는 메서드를 제공합니다.
 * </p>
 */
public class MyMqttMessage extends Message {
    byte[] payload;
    String topic;
    UUID senderId;

    /**
     * MyMqttMessage 클래스의 생성자
     * 
     * @param senderId 발신자의 UUID
     * @param topic    메시지의 토픽
     * @param payload  메시지의 payload
     */
    public MyMqttMessage(UUID senderId, String topic, byte[] payload) {
        this.payload = Arrays.copyOf(payload, payload.length);
        this.topic = topic;
        this.senderId = senderId;
    }

    /**
     * 발신자의 UUID를 반환하는 메서드
     * 
     * @return 발신자의 UUID
     */
    public UUID getSenderId() {
        return senderId;
    }

    /**
     * 메시지의 payload를 반환하는 메서드
     * 
     * @return 메시지의 payload
     */
    public byte[] getPayload() {
        return payload;
    }

    /**
     * 메시지의 토픽을 반환하는 메서드
     * 
     * @return 메시지의 토픽
     */
    public String getTopic() {
        return topic;
    }

    /**
     * 객체를 문자열로 표현하는 메서드
     * 
     * @return payload를 문자열로 변환한 값
     */
    @Override
    public String toString() {
        return new String(payload) + Arrays.toString(payload);
    }
}
