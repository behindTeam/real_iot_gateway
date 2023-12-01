package com.front.node;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import com.front.message.MyMqttMessage;
import com.front.wire.Wire;

public class MqttInNode extends InputOutputNode {
    Wire outputWire;
    private static final Logger logger = LogManager.getLogger(MqttInNode.class);

    public MqttInNode() {
        this(1, 1);
    }

    public MqttInNode(int inCount, int outCount) {
        super(inCount, outCount);
    }

    @Override
    void preprocess() {
        outputWire = getOutputWire(0);
    }

    @Override
    void process() {
        UUID cunnetId = UUID.randomUUID();
        try (IMqttClient serverClient =
                new MqttClient("tcp://ems.nhnacademy.com", cunnetId.toString())) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            serverClient.connect(options);

            serverClient.subscribe("application/+/device/+/+/up", (topic, msg) -> {
                MyMqttMessage mqttmessage = new MyMqttMessage(cunnetId, topic, msg.getPayload());
                output(mqttmessage);
            });

            while (!Thread.currentThread().isInterrupted()) {
                sleepForInterval();
            }

            serverClient.disconnect();
        } catch (MqttException e) {
            Thread.currentThread().interrupt();
            logger.error("MqttException", e);
        }


    }

    public void sleepForInterval() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        preprocess();
        process();
        postprocess();
    }
}
