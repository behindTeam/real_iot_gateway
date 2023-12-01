package com.front.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.front.message.JsonMessage;
import com.front.message.Message;
import com.front.message.StringArrayMessage;
import com.front.node.ProcessCommandLineNode;
import com.front.wire.BufferedWire;
import com.front.wire.Wire;

public class ComandLineArgumetsTest {
    private static final Logger logger = LogManager.getLogger(ComandLineArgumetsTest.class);

    public static void main(String[] args) {
        Message argMessage = new StringArrayMessage(args);
        Wire wire1 = new BufferedWire();
        wire1.put(argMessage);
        Wire wire2 = new BufferedWire();

        ProcessCommandLineNode node = new ProcessCommandLineNode();

        node.connectInputWire(0, wire1);
        node.connectOutputWire(0, wire2);
        node.start();
        node.join();
        JsonMessage message = (JsonMessage) wire2.get();
        logger.info(message);
    }
}
