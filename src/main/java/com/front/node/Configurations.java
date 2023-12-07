package com.front.node;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONArray;

// ... (다른 import 구문들은 동일하게 유지)

public class Configurations {
    static int count = 0;

    public static void main(String[] args) throws FileNotFoundException {
        JSONParser parser = new JSONParser(); // JSON 파일 읽기
        Reader reader = new FileReader("src/main/java/com/front/data.json");
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            List<Node> arr = new ArrayList<>();
            JSONArray nodeList = (JSONArray) jsonArray.get(0);
            // JSONArray로부터 원하는 데이터 추출
            for (Object obj : nodeList) {
                JSONObject jsonObject = (JSONObject) obj;

                String mqttInNodeName = "com.front.node.MqttInNode";
                String wireName = "com.front.wire.BufferedWire";
                String messageParsingNodeName = "com.front.node.MessageParsingNode";
                String mqttOutNodeName = "com.front.node.MqttOutNode";
                // Map<Node, Wire[]> map = new HashMap<>();

                switch (jsonObject.get( "type").toString()) {
                    case "mqtt in":
                        createNodeInstance(jsonObject, mqttInNodeName, arr);
                        break;
                    case "messageParsing":
                        createNodeInstance(jsonObject, messageParsingNodeName, arr);
                        break;
                    case "mqtt out":
                        createNodeInstance(jsonObject, mqttOutNodeName, arr);
                        break;
                    default:
                }
                // System.out.println(jsonObject.get("id").toString());
            }

            for (Node node : arr) {
                switch () {
                    case value:
                        
                        break;

                    default:
                        break;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void createNodeInstance(JSONObject jsonObject, String nodeName, List<Node> arr) {
        try {
            Class<?> clazz = Class.forName(nodeName);
            Node node = (Node) clazz.getDeclaredConstructor().newInstance(); // 노드 생성

            Method setNameMethod = clazz.getMethod("setName", String.class);

            setNameMethod.invoke(node, jsonObject.get("id")); // jsonObject.Id를
            arr.add(node);
            // name으로 할당

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
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