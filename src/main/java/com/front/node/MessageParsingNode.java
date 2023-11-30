package com.front.node;

import java.io.FileReader;
import java.util.Date;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.fasterxml.jackson.core.JsonParser;
import com.front.message.JsonMessage;
import com.front.message.Message;
import com.front.test.LocalSend;
import com.front.wire.Wire;

public class MessageParsingNode extends InputOutputNode{

    String ApplicationName;
    String[] sensor;
    JSONParser parser;
    JSONObject inputMessage;

    public MessageParsingNode(){
        this(1, 1);
    }

    public MessageParsingNode(int inCount, int outCount) {
        super(inCount, outCount);
        parser = new JSONParser();
    }

    @Override
    void preprocess() {
        Wire inputWire = getInputWire(0);
        JsonMessage message = (JsonMessage)inputWire.get();
        inputMessage = message.getPayload();
    }

    @Override
    void process() {

        JsonMessage jsonMessage = null;
        // jsonMessage = new JsonMessage();

        output(jsonMessage);
    }

    @Override
    void postprocess() {
        //
    }
    
}
