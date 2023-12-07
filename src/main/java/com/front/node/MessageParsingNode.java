package com.front.node;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
        settings = settingMessage.getPayload();
    }

    @Override
    void process() {
        if ((getInputWire(1) != null) && (getInputWire(1).hasMessage())) {
            Message myMqttMessage = getInputWire(1).get();
            if (myMqttMessage instanceof MyMqttMessage &&  (Objects.nonNull(((MyMqttMessage) myMqttMessage).getPayload()))) {
                    messageParsing((MyMqttMessage) myMqttMessage);
                
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

            StringBuilder sb = new StringBuilder();
            sb.append("data");

            if (deviceInfo != null) {
                Object tag = deviceInfo.get("tags");
                if (tag instanceof JSONObject) {
                    for (Object key : ((JSONObject) tag).keySet()) {
                        switch (key.toString()) {
                            case "site":
                                sb.append("/s/" + ((JSONObject) tag).get("site"));
                                break;
                            case "name":
                                sb.append("/n/" + ((JSONObject) tag).get("name"));
                                break;
                            case "branch":
                                sb.append("/b/" + ((JSONObject) tag).get("branch"));
                                break;
                            case "place":
                                sb.append("/p/" + ((JSONObject) tag).get("place"));
                                break;
                            default:
                        }
                    }
                }

            }

            long currentTime = new Date().getTime();

            if (object != null) {
                for (Object sensorType : object.entrySet()) {
                    if (deviceInfo.get("applicationName").equals(settings.get("applicationName"))) {
                        String sensor = (String) settings.get("sensor");
                        if (settings.get("sensor") != null) {
                            String[] sensors = sensor.split(",");
                            if (sensor.contains(sensorType.toString()))
                                for (String s : sensors) {
                                    Map<String,Object> data = new HashMap<>();
                                    Map<String,Object> sendMessage = new HashMap<>();
                                    data.put("time", currentTime);
                                    data.put("value", object.get(s));
                                    sendMessage.put("payload", data);
                                    JSONObject newMessage = new JSONObject(sendMessage);
                                    output(new MyMqttMessage(myMqttMessage.getSenderId(), sb.toString() + "/e/" + s, newMessage.toJSONString().getBytes()));
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
