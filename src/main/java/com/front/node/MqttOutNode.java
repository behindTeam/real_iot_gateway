package com.front.node;

import java.util.Objects;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.front.message.Message;
import com.front.message.MyMqttMessage;
import com.front.wire.Wire;

public class MqttOutNode extends InputOutputNode {
    private static final Logger logger = LogManager.getLogger(MqttOutNode.class);
    Wire inputWire;
    UUID cunnetId;

    public MqttOutNode() {
        this(1, 1);
    }

    public MqttOutNode(int inCount, int outCount) {
        super(inCount, outCount);
    }

    @Override
    void preprocess() {}

    @Override
    void process() {
        if (getInputWire(0) != null && getInputWire(0).hasMessage()) {
            Message myMqttMessage = getInputWire(0).get();
            if (myMqttMessage instanceof MyMqttMessage
                    && Objects.nonNull(((MyMqttMessage) myMqttMessage).getPayload())) {
                publish((MyMqttMessage) myMqttMessage);
            }
        }
    }


    public void publish(MyMqttMessage inMessage) {
        cunnetId = UUID.randomUUID();
        try (IMqttClient localClient = new MqttClient("tcp://localhost", cunnetId.toString())) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            localClient.connect(options);
            localClient.publish(inMessage.getTopic(), new MqttMessage(inMessage.getPayload()));
            localClient.disconnect();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
