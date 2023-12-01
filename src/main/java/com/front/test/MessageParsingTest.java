package com.front.test;

import com.front.message.JsonMessage;
import com.front.message.Message;
import com.front.message.StringArrayMessage;
import com.front.node.MessageParsingNode;
import com.front.node.MqttNode;
import com.front.node.ProcessCommandLineNode;
import com.front.wire.BufferedWire;
import com.front.wire.Wire;

public class MessageParsingTest {

    public static void main(String[] args) throws InterruptedException {

        Message argMessage = new StringArrayMessage(args);
        Wire wire1 = new BufferedWire();
        Wire wire2 = new BufferedWire();
        Wire wire3 = new BufferedWire();
        Wire wire4 = new BufferedWire();

        ProcessCommandLineNode node = new ProcessCommandLineNode();
        MqttNode mqttNode = new MqttNode();
        MessageParsingNode msgParsingNode = new MessageParsingNode();

        wire1.put(argMessage);

        node.connectInputWire(0, wire1);
        node.connectOutputWire(0, wire2);
        mqttNode.connectOutputWire(0, wire3);
        msgParsingNode.connectInputWire(0, wire2);
        msgParsingNode.connectInputWire(1, wire3);
        msgParsingNode.connectOutputWire(0, wire4);

        node.start();
        node.join();
        mqttNode.start();
        msgParsingNode.start();

        // Wire wire2 = new BufferedWire();

        // MqttNode node = new MqttNode();
        // node.connectOutputWire(0, wire2);
        // node.start();
    }

}
