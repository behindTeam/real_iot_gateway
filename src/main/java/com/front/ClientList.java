package com.front;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttClient;

public class ClientList {

    private Map<String, IMqttClient> map;

    private static final ClientList clientList = new ClientList();

    private ClientList() {
        map = new HashMap<>();
    }

    public static ClientList getClientList() {
        return clientList;
    }

    public IMqttClient getClient(String id) {
        return map.get(id);
    }

    public void setClient(String id, IMqttClient client) {
        map.put(id, client);
    }
}
