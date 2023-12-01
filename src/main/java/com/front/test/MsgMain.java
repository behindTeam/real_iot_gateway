package com.front.test;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MsgMain {
    public static void main(String[] args) {
        String cunnetId = UUID.randomUUID().toString();
        MqttMessage message = new MqttMessage();
        // server, local mqtt연결
        try (IMqttClient serverClient = new MqttClient("tcp://ems.nhnacademy.com", cunnetId);
                IMqttClient localClient = new MqttClient("tcp://localhost:1883", cunnetId)) {
            MqttConnectOptions options = new MqttConnectOptions();
            // option 설정
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            // option connect
            serverClient.connect(options);
            localClient.connect(options);

            serverClient.subscribe("application/+/device/+/+/up", (topic, msg) -> {
                MessegeParser mp = new MessegeParser(msg);
                mp.getKey();
                mp.setPayload();
            });

            while (!Thread.currentThread().interrupted()) {
                Thread.sleep(100);
            }

            serverClient.disconnect();
            localClient.disconnect();

        } catch (MqttException e) {
            System.err.println("MqttException");
        } catch (InterruptedException e) {
            System.err.println("InterruptedException");
        }
    }
}