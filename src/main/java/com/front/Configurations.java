package com.front;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.front.node.InputOutputNode;
import com.front.node.Node;
import com.front.wire.Wire;

import org.json.simple.JSONArray;

public class Configurations {
    static int count = 0;
    private static Map<Node, JSONArray> map = new HashMap<>();
    private static Map<String, Node> nodeMap = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        JSONParser parser = new JSONParser(); // JSON 파일 읽기
        Reader reader = new FileReader("src/main/java/com/front/data.json");

        String mqttInNodeName = "com.front.node.MqttInNode";
        String messageParsingNodeName = "com.front.node.MessageParsingNode";
        String mqttOutNodeName = "com.front.node.MqttOutNode";

        try {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            JSONArray nodeList = (JSONArray) jsonArray.get(0);
            // JSONArray로부터 원하는 데이터 추출

            for (Object obj : nodeList) {
                JSONObject jsonObject = (JSONObject) obj;

                switch (jsonObject.get("type").toString()) {
                    case "mqtt in":
                        createNodeInstance(jsonObject, mqttInNodeName);

                        break;
                    case "messageParsing":
                        createNodeInstance(jsonObject, messageParsingNodeName);
                        break;
                    case "mqtt out":
                        createNodeInstance(jsonObject, mqttOutNodeName);
                        break;
                    default:
                }
            }

            for (Node before : map.keySet()) {
                JSONArray idList = map.get(before);
                for (Object id : idList) {
                    Node after = nodeMap.get((String) id);
                    connect(before, after);
                }
            }

            for (Node node : map.keySet()) {
                ((InputOutputNode) node).start();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void createNodeInstance(JSONObject jsonObject, String nodeName) {
        try {
            Class<?> clazz = Class.forName(nodeName);
            Node node = (Node) clazz.getDeclaredConstructor().newInstance(); // 노드 생성

            Method setNameMethod = clazz.getMethod("setName", String.class);

            setNameMethod.invoke(node, jsonObject.get("id")); // jsonObject.Id를

            if (!((JSONArray) ((JSONArray) jsonObject.get("wires"))).isEmpty()) {
                map.put(node, (JSONArray) ((JSONArray) jsonObject.get("wires")).get(0));
            }

            nodeMap.put((jsonObject.get("id")).toString(), node);

            switch (nodeName) {
                case "messageParsing":
                    configureSettings
                    break;

                default:
                    break;
            }

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void connect(Node before, Node after) {
        String wireName = "com.front.wire.BufferedWire";
        String nodeName = "com.front.node.InputOutputNode";

        try {

            Class<?> clazz = Class.forName(wireName);
            Object wire = clazz.getDeclaredConstructor().newInstance();

            Class<?> nodeClazz = Class.forName(nodeName);
            Method connectOutputWireMethod = nodeClazz.getMethod("connectOutputWire", int.class, Wire.class); // 메소드
                                                                                                              // 호출
            Method connectInputWireMethod = nodeClazz.getMethod("connectInputWire", int.class, Wire.class); // 메소드
                                                                                                            // 호출

            connectOutputWireMethod.invoke((InputOutputNode) before, 0, wire);
            connectInputWireMethod.invoke((InputOutputNode) after, 0, wire);

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            System.err.println(e);
        }

    }
}

// BiConsumer<Node, Node> setConnect = {(input, output) -> new Wire wire = new
// Wire()

// Method connectOutputWireMethod =
// clazz.getDeclaredMethod("connectOutputWire"); // 메소드 호출

// if (outputWire() != null) {
// output.connectInputWire();
// }
// input.connectOutputWire();
// }

// Class<?> clazz1 = Class.forName(wireName);
// Object wire = clazz1.getDeclaredConstructor().newInstance();

// connectOutputWireMethod.invoke(mqttInNode, 0, wire);