package com.front.node;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import com.front.message.MyMqttMessage;
import com.front.wire.Wire;

/**
 * {@code MqttNode}는 MQTT 브로커와 연결하여 메시지를 수신하는 역할을 하는 노드입니다.
 * 
 * <p>
 * {@code MqttNode}는 {@code InputOutputNode}를 상속하며, MQTT 브로커와 연결하여 메시지를 수신하고
 * 해당 메시지를 출력 와이어로 전송합니다.
 * </p>
 * 
 * <p>
 * MQTT 브로커와의 연결, 메시지 수신 등의 동작은 {@code process()} 메서드에서 실행됩니다.
 * </p>
 * 
 * 
 */
public class MqttNode extends InputOutputNode {
    Wire outputWire;

    /**
     * 입력 및 출력 와이어 개수를 기본값으로 설정합니다.
     */
    public MqttNode() {
        this(1, 1);
    }

    public MqttNode(int inCount, int outCount) {
        super(inCount, outCount);
    }

    @Override
    void preprocess() {
        outputWire = getOutputWire(0);
    }

    /**
     * MQTT 브로커와 연결하여 메시지를 수신하고, 해당 메시지를 출력 와이어로 전송하는 메서드입니다.
     * 
     * @throws InterruptedException
     */
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

            while (!Thread.currentThread().interrupted()) {
                Thread.sleep(100);
            }

            serverClient.disconnect();
        } catch (Exception e) {
            System.err.println("");
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
