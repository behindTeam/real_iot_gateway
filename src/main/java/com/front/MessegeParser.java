package com.front;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.parser.JSONParser;

public class MessegeParser {
    MqttMessage messege;
    JSONParser jsonParser;

    public MessegeParser(MqttMessage msg){
        this.messege = msg;
        jsonParser = new JSONParser();
    }

    public String getTopic(){

        return null;
    }

    public void messageParser(){
        messege.getPayload();
    }

    public MqttMessage printMessage(){
        return messege;
    }
}
