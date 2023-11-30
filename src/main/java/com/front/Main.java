package com.front;

import java.io.FileReader;
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
                IMqttClient infomation = new MqttClient("tcp://ems.nhnacademy.com:1883", publisherId)) {
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

                    if (deviceInfo != null) {
                        Object tag = deviceInfo.get("tags");
                        if (tag instanceof JSONObject) {
                            for (Object key : ((JSONObject) tag).keySet()) {
                                switch (key.toString()) {
                                    case "site":
                                        commonTopic += "/s/" + ((JSONObject) tag).get("site");
                                        break;
                                    case "name":
                                        commonTopic += "/n/" + ((JSONObject) tag).get("name");
                                        break;
                                    case "branch":
                                        commonTopic += "/b/" + ((JSONObject) tag).get("branch");
                                        break;
                                    case "place":
                                        commonTopic += "/p/" + ((JSONObject) tag).get("place");
                                        break;
                                    default:
                                }
                            }
                        }

                    }

                    long currentTime = new Date().getTime();

                    Object jsonFile = (JSONObject) parser
                            .parse(new FileReader("src/main/resources/index.json"));

                    JSONObject jsonObject = (JSONObject) jsonFile;

                    if (object != null) {
                        for (Object sensorType : object.keySet()) {

                            String applicationName = (String) jsonObject.get("applicationName");

                            if (deviceInfo.get("applicationName").equals(applicationName)) {

                                String sensor = (String) jsonObject.get("sensor");

                                if (sensor != null) {
                                    String[] sensors = sensor.split(",");

                                    System.out.println("Sensors:");
                                    if (sensor.contains(sensorType.toString()))
                                        for (String s : sensors) {
                                            System.out.println(s.trim());

                                            JSONObject sensorData = new JSONObject();
                                            sensorData.put("time", currentTime);
                                            sensorData.put("value", object.get(sensorType));

                                            JSONObject newMessage = new JSONObject();
                                            newMessage.put("payload", sensorData);

                                            MqttMessage message = new MqttMessage(
                                                    newMessage.toJSONString().getBytes());
                                            client.publish(commonTopic + "/m/" + sensorType,
                                                    message);

                                        }
                                } else {
                                    System.out.println("센서없음");
                                }
                            } else {
                                System.out.println("어플리케이션없음");
                            }

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            while ((!Thread.currentThread().isInterrupted())) {
                Thread.sleep(100);
            }

            client.disconnect();
            infomation.disconnect();

        } catch (MqttException |

                InterruptedException e) {
            e.printStackTrace();
        }
    }
}