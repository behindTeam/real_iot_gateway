package com.front.node;

import com.front.exception.AlreadyExistsException;
import com.front.exception.InvalidArgumentException;
import com.front.exception.OutOfBoundsException;
import com.front.wire.Wire;

/**
 * {@code OutputNode}는 출력 와이어를 가지고 있는 노드의 기본 동작을 정의하는 추상 클래스입니다.
 * 
 * <p>
 * {@code OutputNode}는 {@code ActiveNode}를 상속하며, 출력 와이어의 연결과 관련된 동작을 수행합니다.
 * </p>
 * 
 * <p>
 * 출력 노드는 입력 와이어를 가지고 있으며, 이를 연결하고 연결된 와이어에 메시지를 전달하는 등의 동작을 수행합니다.
 * </p>
 * 
 * <p>
 * 생성자에서는 입력 와이어 개수를 받아 초기화하며, 연결 및 기본 동작 등에 대한 메서드를 포함합니다.
 * </p>
 */
public abstract class OutputNode extends ActiveNode {
    Wire[] inputWires;

    OutputNode(String name, int count) {
        super(name);
        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        inputWires = new Wire[count];
    }

    OutputNode(int count) {
        super();
        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        inputWires = new Wire[count];
    }

    public void connectInputWire(int index, Wire wire) {
        if (inputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        if (inputWires[index] != null) {
            throw new AlreadyExistsException();
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
}
