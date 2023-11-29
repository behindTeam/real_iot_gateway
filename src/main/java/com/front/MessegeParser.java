package com.front;

import java.util.Date;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessegeParser {
    MqttMessage messege;
    JSONParser jsonParser;
    JSONObject payload;
    

    public MessegeParser(MqttMessage msg) throws ParseException{
        this.messege = msg;
        jsonParser = new JSONParser();
        payload = (JSONObject) jsonParser.parse(new String(msg.getPayload()));
    }

    public JSONObject getDeviceInfo(){
        return (JSONObject) payload.get("deviceInfo");
    }

    public JSONObject getObject(){
        return (JSONObject) payload.get("object");
    }

    public String getKey(){
        String commonTopic = "data";
        Object tag = getDeviceInfo().get("tags");
        if (tag instanceof JSONObject) {
            for (Object key : ((JSONObject)tag).keySet()) {
                switch (key.toString()) {
                    case "site":
                        commonTopic += "/s/" + ((JSONObject)tag).get("site");
                        break;
                    case "name":
                        commonTopic += "/n/" + ((JSONObject)tag).get("name");
                        break;
                    case "branch":
                        commonTopic += "/b/" + ((JSONObject)tag).get("branch");
                        break;
                    case "place":
                        commonTopic += "/p/" + ((JSONObject)tag).get("place");
                        break;
                    default:
                }
            }
        }
        return commonTopic;
    }

    public JSONObject getPayload(){
        long currentTime = new Date().getTime();
        JSONArray payloadArray = new JSONArray();
        if (getObject() != null) {
            for (Object sensorType : getObject().keySet()) {
                JSONObject newMessage = new JSONObject();
                newMessage.put("topic", getKey() + "/e/" + sensorType);
                JSONObject sensorData = new JSONObject();
                newMessage.put("payload", sensorData);
                sensorData.put("time", currentTime);
                sensorData.put("value", getObject().get(sensorType));

                payloadArray.add(newMessage);
            }
        }
        return null;
    }

    public void messageParser(){
        messege.getPayload();
    }

    public MqttMessage printMessage(){
        return messege;
    }
}
