package com.front.node;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONArray;

// ... (다른 import 구문들은 동일하게 유지)

public class SettingNode {
    public static void main(String[] args) throws FileNotFoundException {
        JSONParser parser = new JSONParser(); // JSON 파일 읽기
        Reader reader = new FileReader("src/main/java/com/front/setting.json");
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            // JSONArray로부터 원하는 데이터 추출
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                switch (jsonObject.get("type").toString()) {
                    case "mqtt in":

                        break;
                    case "function":

                        break;
                    case "mqtt out":

                        break;
                    default:

                }
                System.out.println(jsonObject.get("id").toString());
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
