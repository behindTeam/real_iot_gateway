package com.front.test;

import com.front.node.MqttNode;
import com.front.wire.BufferedWire;
import com.front.wire.Wire;

public class MqttNodeTest {
    public static void main(String[] args) {
        Wire wire2 = new BufferedWire();

        MqttNode node = new MqttNode();
        node.connectOutputWire(0, wire2);
        node.start();

    }
}
