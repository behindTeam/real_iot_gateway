package com.front;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String publisherId = UUID.randomUUID().toString();

        try (IMqttClient client = new MqttClient("tcp://ems.nhnacademy.com", publisherId);
                IMqttClient client2 = new MqttClient("tcp://localhost", publisherId)) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setWill("test/will", "Disconnected".getBytes(), 1, false);
            options.setKeepAliveInterval(1000);

            client.connect(options);
            client2.connect(options);

            MqttMessage message = new MqttMessage();

            client.subscribe("application/+/device/+/event/up",
                    (topic, msg) -> {
                        System.out.println("Message received : " + topic);
                        message.setPayload(msg.getPayload());
                    });

            while (!Thread.currentThread().interrupted()) {
                byte[] byteArray = message.getPayload();
                String jsonString = new String(byteArray);

                JSONParser parser = new JSONParser();
                try {
                    Object obj = parser.parse(jsonString);
                    JSONObject jsonObject = (JSONObject) obj;

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // client2.publish("test/" + new String(message.getPayload()).length(),
                // jsonObject.);
            }
            // while (message.getPayload() != null) {
            // client2.publish("test/a/b/c", message);
            // }

            while (Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }
            // client.setCallback(null);

            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}