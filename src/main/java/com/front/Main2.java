package com.front;

import org.json.simple.JSONObject;

import com.front.message.JsonMessage;
import com.front.message.Message;
import com.front.message.StringArrayMessage;
import com.front.node.ProcessCommandLineNode;
import com.front.wire.BufferedWire;
import com.front.wire.Wire;

public class Main2 {
    public static void main(String[] args) {
        Message argMessage = new StringArrayMessage(args);
        Wire wire1 = new BufferedWire();
        wire1.put(argMessage);

        ProcessCommandLineNode node = new ProcessCommandLineNode();
        node.connectInputWire(0, wire1);

    }
}
