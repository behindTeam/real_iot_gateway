package com.front.test;

import com.front.message.JsonMessage;
import com.front.message.Message;
import com.front.message.StringArrayMessage;
import com.front.node.MqttInNode;
import com.front.node.ProcessCommandLineNode;
import com.front.wire.BufferedWire;
import com.front.wire.Wire;

public class MqttNodeTest {
    public static void main(String[] args) {
        Wire wire2 = new BufferedWire();

        MqttInNode node = new MqttInNode();
        node.connectOutputWire(0, wire2);
        node.start();
    }
}
