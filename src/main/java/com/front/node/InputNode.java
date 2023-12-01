package com.front.node;

import org.json.simple.JSONObject;

import com.front.exception.AlreadyExistsException;
import com.front.exception.InvalidArgumentException;
import com.front.exception.OutOfBoundsException;
import com.front.message.Message;
import com.front.wire.Wire;

/**
 * {@code InputNode}는 {@code ActiveNode}를 상속하며, 입력 노드의 기본 동작을 정의하는 추상 클래스입니다.
 * 이 클래스는 출력 와이어를 연결하고, 메시지를 해당 와이어로 전송하는 기능을 제공합니다.
 * 
 * <p>
 * {@code InputNode}는 추상 클래스로, 구체적인 입력 노드 타입은 이 클래스를 상속하여 구현해야 합니다.
 * </p>
 *
 */
public abstract class InputNode extends ActiveNode {

    Wire[] outputWires;

    InputNode(JSONObject json) {
        super(json);
    }

    /**
     * 이름과 출력 와이어 개수를 인자로 받는 생성자로, 상위 클래스의 이름을 설정하는 생성자를 호출하고,
     * 출력 와이어 배열을 초기화합니다.
     * 만약 출력 와이어 개수가 0 이하인 경우 {@code InvalidArgumentException}을 발생시킵니다.
     *
     * @param name  노드의 이름
     * @param count 출력 와이어 개수
     */
    InputNode(String name, int count) {
        super(name);

        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        outputWires = new Wire[count];
    }

    InputNode(int count) {
        super();

        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        outputWires = new Wire[count];
    }

    /**
     * 지정된 인덱스에 출력 와이어를 연결합니다.
     * 만약 인덱스가 배열 범위를 벗어나거나 이미 해당 인덱스에 와이어가 연결된 경우
     * {@code OutOfBoundsException} 또는 {@code AlreadyExistsException}을 발생시킵니다.
     *
     * @param index 연결할 와이어의 인덱스
     * @param wire  연결할 와이어 객체
     */
    public void connectOutputWire(int index, Wire wire) {
        if (outputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        if (outputWires[index] != null) {
            throw new AlreadyExistsException();
        }

        outputWires[index] = wire;
    }

    public int getOutputWireCount() {
        return outputWires.length;
    }

    public Wire getOutputWire(int index) {
        if (index < 0 || outputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        return outputWires[index];
    }

    void output(Message message) {
        log.trace("Message Out");
        for (Wire wire : outputWires) {
            if (wire != null) {
                wire.put(message);
            }
        }
    }
}
