package com.front.node;

import com.front.exception.OutOfBoundsException;
import com.front.message.Message;
import com.front.wire.Wire;

/**
 * {@code InputOutputNode}는 {@code ActiveNode}를 상속하며, 입출력 노드의 기본 동작을 정의하는 추상
 * 클래스입니다.
 * 이 클래스는 입출력 와이어를 연결하고, 메시지를 해당 와이어로 전송하는 기능을 제공합니다.
 * 
 * <p>
 * {@code InputOutputNode}는 추상 클래스로, 구체적인 입출력 노드 타입은 이 클래스를 상속하여 구현해야 합니다.
 * </p>
 * 
 * <p>
 * 입출력 와이어 개수는 생성자를 통해 결정되며, 와이어를 연결하고 조회하는 메서드를 제공합니다.
 * </p>
 * 
 * <p>
 * 이 클래스는 {@code ActiveNode}의 기능을 확장하여 입력과 출력을 함께 다룰 수 있도록 합니다.
 * </p>
 */
public abstract class InputOutputNode extends ActiveNode {

    Wire[] inputWires;
    Wire[] outputWires;

    /**
     * 이름, 입력 와이어 개수, 출력 와이어 개수를 인자로 받는 생성자로,
     * 상위 클래스의 이름을 설정하는 생성자를 호출하고, 입력 및 출력 와이어 배열을 초기화합니다.
     *
     * @param name     노드의 이름
     * @param inCount  입력 와이어 개수
     * @param outCount 출력 와이어 개수
     */
    InputOutputNode(String name, int inCount, int outCount) {
        super(name);

        inputWires = new Wire[inCount];
        outputWires = new Wire[outCount];
    }

    InputOutputNode(int inCount, int outCount) {
        super();

        inputWires = new Wire[inCount];
        outputWires = new Wire[outCount];
    }

    /**
     * 지정된 인덱스에 출력 와이어를 연결합니다.
     * 만약 인덱스가 배열 범위를 벗어나면 {@code OutOfBoundsException}을 발생시킵니다.
     *
     * @param index 연결할 와이어의 인덱스
     * @param wire  연결할 와이어 객체
     */
    public void connectOutputWire(int index, Wire wire) {
        if (index < 0 || outputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        outputWires[index] = wire;
    }

    public int getOutputWireCount() {
        return outputWires.length;
    }

    /**
     * 지정된 인덱스의 출력 와이어 객체를 반환합니다.
     * 만약 인덱스가 배열 범위를 벗어나면 {@code OutOfBoundsException}을 발생시킵니다.
     *
     * @param index 반환할 와이어의 인덱스
     * @return 지정된 인덱스의 출력 와이어 객체
     */
    public Wire getOutputWire(int index) {
        if (index < 0 || outputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        return outputWires[index];
    }

    /**
     * 지정된 인덱스에 입력 와이어를 연결합니다.
     * 만약 인덱스가 배열 범위를 벗어나면 {@code OutOfBoundsException}을 발생시킵니다.
     *
     * @param index 연결할 와이어의 인덱스
     * @param wire  연결할 와이어 객체
     */
    public void connectInputWire(int index, Wire wire) {
        if (index < 0 || inputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        inputWires[index] = wire;
    }

    public int getInputWireCount() {
        return inputWires.length;
    }

    public Wire getInputWire(int index) {
        if (index < 0 || inputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        return inputWires[index];
    }

    void output(Message message) {
        log.trace("Message Out");
        for (Wire port : outputWires) {
            if (port != null) {
                port.put(message);
            }
        }
    }
}
