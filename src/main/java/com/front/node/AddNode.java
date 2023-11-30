package com.front.node;

import com.front.message.LongMessage;

public class AddNode extends BinaryOperationNode {
    public AddNode() {
        super();
    }

    public AddNode(String name) {
        super(name);
    }

    @Override
    void process() {
        if (((getInputWire(0) != null) && (getInputWire(0).hasMessage()))
                && ((getInputWire(1) != null) && (getInputWire(1).hasMessage()))) {

            long sum = ((LongMessage) getInputWire(0).get()).getPayload()
                    + ((LongMessage) getInputWire(1).get()).getPayload();

            output(new LongMessage(sum));
        }
    }
}
