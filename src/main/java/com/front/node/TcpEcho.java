package com.front.node;

import com.front.message.TcpRequestMessage;
import com.front.message.TcpResponseMessage;

public class TcpEcho extends InputOutputNode {
    public TcpEcho() {
        super(1, 1);
    }

    public TcpEcho(String name) {
        super(name, 1, 1);
    }

    @Override
    void process() {
        if ((getInputWire(0) != null) && getInputWire(0).hasMessage()) {
            TcpRequestMessage request = (TcpRequestMessage) getInputWire(0).get();
            output(new TcpResponseMessage(request.getSenderId(), request.getPayload()));
        }
    }
}
