package com.front;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.fasterxml.jackson.annotation.JsonAlias;

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
        JSONObject deviceInfo = (JSONObject) payload.get("deviceInfo");
        return deviceInfo;
    }

    public String getKey(){
        if (getDeviceInfo() != null) {
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
