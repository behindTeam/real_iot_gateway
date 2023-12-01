package com.front.node;

import java.util.Date;
import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.front.message.JsonMessage;
import com.front.message.Message;
import com.front.message.MyMqttMessage;
import com.front.wire.Wire;

/**
 * {@code MessageParsingNode}는 입력 및 출력을 다루며, MQTT 메시지를 파싱하여 필요한 정보를 추출하는 노드입니다.
 * 입력으로는 설정 메시지와 MQTT 메시지를 받아 처리하고, 출력으로는 필요한 정보를 전달합니다.
 * 
 * <p>
 * {@code MessageParsingNode}는 {@code InputOutputNode}를 상속하며, 추상 클래스인
 * {@code InputOutputNode}를 구현합니다.
 * </p>
 * 
 * 
 */
public class MessageParsingNode extends InputOutputNode {

    Wire settingWire;

    Wire mqttWire;
    Message message;

    String applicationName;

    String[] sensor;

    JSONParser parser;

    JSONObject settings;

    /**
     * 기본 생성자로, 입력 및 출력 와이어 개수를 기본값으로 설정합니다.
     */
    public MessageParsingNode() {
        this(2, 1);
    }

    /**
     * 입력 및 출력 와이어 개수를 지정하여 생성하는 생성자입니다.
     * 
     * @param inCount  입력 와이어 개수
     * @param outCount 출력 와이어 개수
     */
    public MessageParsingNode(int inCount, int outCount) {
        super(inCount, outCount);
        parser = new JSONParser();
    }

    /**
     * 전처리 메서드로, 설정 메시지를 받아 설정 정보를 추출합니다.
     */
    @Override
    void preprocess() {
        settingWire = getInputWire(0);
        JsonMessage settingMessage = (JsonMessage) settingWire.get();
        settings = settingMessage.getPayload();
    }

    /**
     * 메시지 처리 메서드로, MQTT 메시지를 파싱하여 필요한 정보를 추출하고 출력 메시지를 생성합니다.
     */
    @Override
    void process() {
        if ((getInputWire(1) != null) && (getInputWire(1).hasMessage())) {
            Message myMqttMessage = getInputWire(1).get();
            if (myMqttMessage instanceof MyMqttMessage) {
                if (Objects.nonNull(((MyMqttMessage) myMqttMessage).getPayload())) {
                    messageParsing((MyMqttMessage) myMqttMessage);
                }
            }
        }
    }

    /**
     * 후처리 메서드로, 추가적인 후처리 동작이 필요한 경우 구현할 수 있습니다.
     */
    @Override
    void postprocess() {
    }

    /**
     * MQTT 메시지를 파싱하여 필요한 정보를 추출하고, 설정된 조건에 따라 출력 메시지를 생성하여 전송합니다.
     * 
     * @param myMqttMessage 파싱할 MQTT 메시지 객체
     */
    public void messageParsing(MyMqttMessage myMqttMessage) {
        try {
            JSONObject payload = (JSONObject) parser.parse(new String(myMqttMessage.getPayload()));

            JSONObject deviceInfo = (JSONObject) payload.get("deviceInfo");
            JSONObject object = (JSONObject) payload.get("object");

            String commonTopic = "data";

            if (deviceInfo != null) {
                Object tag = deviceInfo.get("tags");
                if (tag instanceof JSONObject) {
                    for (Object key : ((JSONObject) tag).keySet()) {
                        switch (key.toString()) {
                            case "site":
                                commonTopic += "/s/" + ((JSONObject) tag).get("site");
                                break;
                            case "name":
                                commonTopic += "/n/" + ((JSONObject) tag).get("name");
                                break;
                            case "branch":
                                commonTopic += "/b/" + ((JSONObject) tag).get("branch");
                                break;
                            case "place":
                                commonTopic += "/p/" + ((JSONObject) tag).get("place");
                                break;
                            default:
                        }
                    }
                }
            }

            long currentTime = new Date().getTime();

            if (object != null) {
                for (Object sensorType : object.keySet()) {
                    if (deviceInfo.get("applicationName").equals(settings.get("applicationName"))) {
                        String sensor = (String) settings.get("sensor");
                        if (settings.get("sensor") != null) {
                            String[] sensors = sensor.split(",");

                            if (sensor.contains(sensorType.toString()))
                                for (String s : sensors) {
                                    JSONObject sensorData = new JSONObject();
                                    sensorData.put("time", currentTime);
                                    sensorData.put("value", object.get(sensorType));

                                    JSONObject newMessage = new JSONObject();
                                    newMessage.put("payload", sensorData);
                                    output(new MyMqttMessage(myMqttMessage.getSenderId(),
                                            commonTopic + "/e/" + sensorType.toString(),
                                            newMessage.toJSONString().getBytes()));
                                }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
