package com.front;

import org.eclipse.paho.client.mqttv3.MqttMessage;
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

    public String getKey(){
        Object tag = getDeviceInfo().get("tags");
        if (tag instanceof JSONObject) {
            for (Object key : ((JSONObject)tag).keySet()) {
                switch (key.toString()) {
                    case "site":
                        System.out.println("site : " + ((JSONObject)tag).get("site"));
                        break;
                    case "name":
                        System.out.println("name : " + ((JSONObject)tag).get("name"));
                        break;
                    case "branch":
                        System.out.println("branch : " + ((JSONObject)tag).get("branch"));
                        break;
                    case "place":
                        System.out.println("place : " + ((JSONObject)tag).get("place"));
                        break;
                    default:
                }
            }
        }
        return "tags 없음";
    }

    public void messageParser(){
        messege.getPayload();
    }

    public MqttMessage printMessage(){
        return messege;
    }
}
