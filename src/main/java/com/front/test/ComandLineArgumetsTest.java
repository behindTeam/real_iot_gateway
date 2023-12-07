package com.front.test;

import com.front.message.Message;
import com.front.message.StringArrayMessage;
import com.front.node.ProcessCommandLineNode;
import com.front.wire.BufferedWire;
import com.front.wire.Wire;

public class ComandLineArgumetsTest {
    public static void main(String[] args){
        Message argMessage = new StringArrayMessage(args);
        Wire wire1 = new BufferedWire();
        wire1.put(argMessage);
        Wire wire2 = new BufferedWire();

        ProcessCommandLineNode node = new ProcessCommandLineNode();

        node.connectInputWire(0, wire1);
        node.connectOutputWire(0, wire2);
        node.start();
        node.join();
    }
}
