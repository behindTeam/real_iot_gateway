package com.front.node;

import java.util.Objects;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.front.message.MyMqttMessage;
import com.front.wire.Wire;

public class MqttOutNode extends InputOutputNode {
    Wire inputWire;
    UUID cunnetId;

    public MqttOutNode() {
        this(1, 1);
    }

    public MqttOutNode(int inCount, int outCount) {
        super(inCount, outCount);
    }

    @Override
    void preprocess() {
        inputWire = getInputWire(0);
    }

    @Override
    void process() {

        if (cunnetId == null) {
            cunnetId = UUID.randomUUID();
            try (IMqttClient localClient =
                    new MqttClient("tcp://localhost:1883", cunnetId.toString())) {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);

                localClient.connect(options);

                while (!Thread.currentThread().interrupted()) {
                    MyMqttMessage inMessage = (MyMqttMessage) inputWire.get();
                    localClient.publish(inMessage.getTopic(),
                            new MqttMessage(inMessage.getPayload()));
                }

                localClient.disconnect();
            } catch (Exception e) {
                System.err.println("");
            }
        }
    }
    // 노드 아웃을 만들었는데 이게 작동 -> sysout이 찍힌다

    @Override
    void postprocess() {}

    @Override
    public void run() {
        preprocess();
        process();
        postprocess();
    }
}
