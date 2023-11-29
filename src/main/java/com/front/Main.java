package com.front;

import java.util.Date;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();

        try (IMqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
                IMqttClient infomation =
                        new MqttClient("tcp://ems.nhnacademy.com:1883", publisherId)) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setWill("test/will", "disconnected".getBytes(), 1, false);
            options.setKeepAliveInterval(1000);

            client.connect(options);
            infomation.connect(options);

            infomation.subscribe("application/+/device/+/event/up", (topic, msg) -> {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject payload = (JSONObject) parser.parse(new String(msg.getPayload()));

                    JSONObject deviceInfo = (JSONObject) payload.get("deviceInfo");
                    JSONObject object = (JSONObject) payload.get("object");

                    String commonTopic = "data";
                    Object senserType = object.keySet().toString().split(",");


                    if (deviceInfo != null) {
                        for (Object element : deviceInfo.keySet()) {
                            String key = (String) element;
                            switch (key) {
                                case "site":
                                    commonTopic += "/s/" + deviceInfo.get(key);
                                    break;
                                case "name":
                                    commonTopic += "/n/" + deviceInfo.get(key);
                                    break;
                                case "branch":
                                    commonTopic += "/b/" + deviceInfo.get(key);
                                    break;
                                case "place":
                                    commonTopic += "/p/" + deviceInfo.get(key);
                                    break;
                            }
                        }
                    }

                    long currentTime = new Date().getTime();
                    JSONArray payloadArray = new JSONArray();

                    if (object != null) {
                        for (Object sensorType : object.keySet()) {
                            JSONObject newMessage = new JSONObject();
                            newMessage.put("topic", commonTopic + "/e/" + sensorType);

                            JSONObject sensorData = new JSONObject();
                            newMessage.put("payload", sensorData);
                            sensorData.put("time", currentTime);
                            sensorData.put("value", object.get(sensorType));

                            payloadArray.add(newMessage);
                        }
                    }


                    String messageNumber = String.valueOf(System.currentTimeMillis());
                    MqttMessage message = new MqttMessage(payloadArray.toJSONString().getBytes());

                    client.publish(commonTopic + "/e/" + "m/" + senserType, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


            while ((!Thread.currentThread().isInterrupted())) {
                Thread.sleep(100);
            }

            client.disconnect();
            infomation.disconnect();


        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
