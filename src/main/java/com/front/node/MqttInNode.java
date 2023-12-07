package com.front.node;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import com.front.message.MyMqttMessage;
import com.front.wire.Wire;

public class MqttInNode extends InputOutputNode {
    Wire outputWire;

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
        try (IMqttClient serverClient = new MqttClient("tcp://ems.nhnacademy.com", cunnetId.toString())) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            serverClient.connect(options);

            serverClient.subscribe("application/+/device/+/+/up", (topic, msg) -> {
                MyMqttMessage mqttmessage = new MyMqttMessage(cunnetId, topic, msg.getPayload());
                output(mqttmessage);
            });

            while (!Thread.interrupted()) {
                Thread.sleep(100);
            }

            serverClient.disconnect();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    void postprocess() {
        //
    }

    @Override
    public void run() {
        preprocess();
        process();
        postprocess();
    }

}
