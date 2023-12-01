package com.front.node;

import java.util.Date;
import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.front.message.JsonMessage;
import com.front.message.Message;
import com.front.message.MyMqttMessage;
import com.front.wire.Wire;

public class MessageParsingNode extends InputOutputNode {
    Wire settingWire;
    Wire mqttWire;
    Message message;

    String ApplicationName;
    String[] sensor;
    JSONParser parser;
    JSONObject settings;

    public MessageParsingNode() {
        this(2, 1);
    }

    public MessageParsingNode(int inCount, int outCount) {
        super(inCount, outCount);
        parser = new JSONParser();
    }

    @Override
    void preprocess() {
        settingWire = getInputWire(0);
        JsonMessage settingMessage = (JsonMessage) settingWire.get();
        if (settingMessage.getPayload() != null) {
            settings = settingMessage.getPayload();
        }
    }

    @Override
    void process() {
        if ((getInputWire(1) != null) && (getInputWire(1).hasMessage())) {
            Message myMqttMessage = getInputWire(1).get();
            if (myMqttMessage instanceof MyMqttMessage) {
                if (Objects.nonNull(((MyMqttMessage) myMqttMessage).getPayload())) {
                    messageParsing((MyMqttMessage) myMqttMessage);
                }
            }
        }
    }

    @Override
    void postprocess() {
        //
    }

    public void messageParsing(MyMqttMessage myMqttMessage) {
        try {
            JSONObject payload = (JSONObject) parser.parse(new String(myMqttMessage.getPayload()));

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

            if (object != null) {
                for (Object sensorType : object.keySet()) {
                    if (deviceInfo.get("applicationName").equals(settings.get("applicationName"))) {
                        String sensor = (String) settings.get("sensor");
                        if (settings.get("sensor") != null) {
                            if (sensor.contains(sensorType.toString())) {

                                JSONObject sensorData = new JSONObject();
                                sensorData.put("time", currentTime);
                                sensorData.put("value", object.get(sensorType));

                                JSONObject newMessage = new JSONObject();
                                newMessage.put("payload", sensorData);
                                output(new MyMqttMessage(myMqttMessage.getSenderId(),
                                        commonTopic + "/e/" + sensorType.toString(),
                                        newMessage.toJSONString().getBytes()));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
