package com.front.test;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LocalSend {
    String topic;
    MqttMessage outputMessage;
    Object sensorType;

    public LocalSend(String topic, MqttMessage message, Object sensorTpye) {
        this.topic = topic;
        this.outputMessage = message;
        this.sensorType = sensorTpye;
    }

    public void run() {
        String cunnetId = UUID.randomUUID().toString();
        try (IMqttClient localClient = new MqttClient("tcp://localhost:1883", cunnetId)) {
            localClient.publish("test/" + topic + "/e/" + sensorType.toString(), outputMessage);

            while (!Thread.currentThread().interrupted()) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
