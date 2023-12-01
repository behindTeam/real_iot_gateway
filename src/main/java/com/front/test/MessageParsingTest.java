package com.front.test;

import com.front.message.Message;
import com.front.message.StringArrayMessage;
import com.front.node.MessageParsingNode;
import com.front.node.MqttInNode;
import com.front.node.MqttOutNode;
import com.front.node.ProcessCommandLineNode;
import com.front.wire.BufferedWire;
import com.front.wire.Wire;

/**
 * {@code MessageParsingTest} 클래스는 메시지 처리 노드들을 연결하여 테스트하는 예제입니다.
 * 
 * <p>
 * 이 테스트는 명령행 인수를 받아와서 처리하는 {@code ProcessCommandLineNode}, MQTT 브로커에서 메시지를 수신하는
 * {@code MqttInNode}, 메시지 파싱을 담당하는 {@code MessageParsingNode}, 그리고 MQTT 브로커로
 * 메시지를 전송하는 {@code MqttOutNode}를 연결하여 실행
 * </p>
 */
public class MessageParsingTest {

    public static void main(String[] args) throws InterruptedException {

        Message argMessage = new StringArrayMessage(args);
        Wire wire1 = new BufferedWire();
        Wire wire2 = new BufferedWire();
        Wire wire3 = new BufferedWire();
        Wire wire4 = new BufferedWire();

        ProcessCommandLineNode node = new ProcessCommandLineNode();
        MqttInNode mqttInNode = new MqttInNode();
        MessageParsingNode msgParsingNode = new MessageParsingNode();
        MqttOutNode mqttOutNode = new MqttOutNode();

        wire1.put(argMessage);

        node.connectInputWire(0, wire1);
        node.connectOutputWire(0, wire2);
        mqttInNode.connectOutputWire(0, wire3);
        msgParsingNode.connectInputWire(0, wire2);
        msgParsingNode.connectInputWire(1, wire3);
        msgParsingNode.connectOutputWire(0, wire4);
        mqttOutNode.connectInputWire(0, wire4);

        node.start();
        node.join();
        mqttInNode.start();
        msgParsingNode.start();
    }

}
