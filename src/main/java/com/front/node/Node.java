package com.front.node;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.github.f4b6a3.uuid.UuidCreator;

/**
 * {@code Node}는 노드의 기본적인 속성 및 동작을 정의하는 추상 클래스입니다.
 * 
 * <p>
 * 모든 노드는 이 클래스를 상속받아 구현되며, 노드의 식별자(ID), 이름, 로깅 등의 기본적인 속성을 가지고 있습니다.
 * </p>
 * 
 * <p>
 * 노드의 생성 및 속성 조회, JSON 형식의 표현을 생성하는 등의 동작을 수행합니다.
 * </p>
 * 
 * <p>
 * 노드의 ID는 UUID(Universal Unique Identifier)를 사용하여 생성하며, 이름은 사용자가 지정할 수 있습니다.
 * </p>
 * 
 * <p>
 * 노드의 로깅은 Log4j를 사용하여 수행하며, 노드가 생성될 때 해당 노드의 정보를 출력합니다.
 * </p>
 * 
 * <p>
 * 노드의 개수는 클래스 변수인 {@code count}를 통해 관리됩니다.
 * </p>
 * 
 * <p>
 * 설정된 노드의 이름 및 속성을 변경하거나 조회할 수 있습니다.
 * </p>
 * 
 * <p>
 * 노드의 JSON 표현을 생성하는 메서드가 포함되어 있습니다.
 * </p>
 */
public abstract class Node {
    private static int count;
    UUID id;
    String name;
    Logger log;

    Node() {
        this(UuidCreator.getTimeBased());
    }

    Node(JSONObject json) {
        if (json.containsKey("id")) {
            id = UuidCreator.fromString((String) json.get("id"));
        } else {
            id = UuidCreator.getTimeBased();
        }
    }

    Node(UUID id) {
        this(id.toString(), id);
    }

    Node(String name) {
        this(name, UuidCreator.getTimeBased());
    }

    Node(String name, UUID id) {
        count++;
        this.id = id;
        this.name = name;
        log = LogManager.getLogger(name);

        log.trace("create node : {}", id);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        log = LogManager.getLogger(name);
    }

    public static int getCount() {
        return count;
    }

    public JSONObject getJson() {
        JSONObject object = new JSONObject();

        object.put("id", id);
        object.put("name", name);

        return object;
    }
}
