package com.front.node;

public class MessageParsingNode extends InputOutputNode{

    public MessageParsingNode(){
        this(1, 1);
    }

    public MessageParsingNode(int inCount, int outCount) {
        super(inCount, outCount);
    }
    
}
