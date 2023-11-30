package com.front;

import org.json.simple.JSONObject;

import com.front.message.JsonMessage;
import com.front.message.Message;

public class Main2 {
    public static void main(String[] args) {
        JSONObject object = new JSONObject();
        object.put("a", "A");
        object.put("b", "B");
        Message jsonMessage = new JsonMessage(object);
        System.out.println(jsonMessage.toString());
    }
}
