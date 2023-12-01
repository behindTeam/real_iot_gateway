package com.front.test;

import com.front.message.JsonMessage;
import com.front.message.Message;
import com.front.message.StringArrayMessage;
import com.front.node.MessageParsingNode;
import com.front.node.ProcessCommandLineNode;
import com.front.wire.BufferedWire;
import com.front.wire.Wire;

/**
 * Command Line Arguments Test 클래스
 */
public class ComandLineArgumetsTest {

    /**
     * 메인 메서드
     * 
     * @param args 커맨드 라인 인자
     * @throws InterruptedException 스레드 대기 중 예외 발생 시
     */
    public static void main(String[] args) throws InterruptedException {
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
        System.out.println(message);
    }
}
