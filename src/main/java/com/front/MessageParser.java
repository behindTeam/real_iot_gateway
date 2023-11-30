package com.front;

import java.util.Date;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageParser {
    MqttMessage messege;
    JSONParser jsonParser;
    JSONObject payload;
    MqttMessage outputmessage;
    String topic;
    Object sensorType;

    public MessageParser(MqttMessage msg) throws ParseException {
        this.messege = msg;
        jsonParser = new JSONParser();
        payload = (JSONObject) jsonParser.parse(new String(msg.getPayload()));
    }

    public JSONObject getDeviceInfo() {
        return (JSONObject) payload.get("deviceInfo");
    }

    public JSONObject getObject() {
        return (JSONObject) payload.get("object");
    }

    public String getTopic() {
        return topic;
    }

    public void getKey() {
        String commonTopic = "data";
        Object tag = getDeviceInfo().get("tags");
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
        this.topic = commonTopic;
    }

    public void setPayload() {
        if (getObject() != null) {
            for (Object sensorType : getObject().keySet()) {
                setSenorType(sensorType);
                JSONObject sensorData = new JSONObject();
                sensorData.put("time", new Date().getTime());
                sensorData.put("value", getObject().get(sensorType));
                JSONObject newMessage = new JSONObject();
                newMessage.put("payload", sensorData);
                MqttMessage message = new MqttMessage(newMessage.toJSONString().getBytes());
                this.outputmessage = message;
            }
        }
    }

    public void setSenorType(Object sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorType() {
        return sensorType.toString();
    }

    public MqttMessage getPayload() {
        return outputmessage;
    }

    public void messageParser() {
        messege.getPayload();
    }

    public MqttMessage printMessage() {
        return messege;
    }
}