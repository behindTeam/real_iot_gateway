package com.front.node;

import org.json.simple.JSONObject;

import com.front.message.BooleanMessage;
import com.front.message.LongMessage;

public class TimerNode extends InputNode {
    public enum Mode {
        TIME,
        COUNT,
        CLOCK
    }

    long count = 0;
    Mode mode = Mode.TIME;
    boolean status = false;

    public TimerNode(JSONObject json) {
        super(json);
        if (json.containsKey("mode")) {

        }
    }

    public TimerNode(String name) {
        super(name, 1);
    }

    public TimerNode(String name, Mode mode) {
        super(name, 1);
        this.mode = mode;
    }

    public TimerNode(Mode mode) {
        super(1);
        this.mode = mode;
    }

    @Override
    void preprocess() {
        if (mode.equals(Mode.CLOCK)) {
            interval = interval / 2;
        }
    }

    @Override
    void process() {
        if (mode.equals(Mode.COUNT)) {
            output(new LongMessage(++count));
        } else if (mode.equals(Mode.CLOCK)) {
            status = !status;
            output(new BooleanMessage(status));
        } else {
            output(new LongMessage(System.currentTimeMillis()));
        }
    }

    @Override
    public JSONObject getJson() {
        JSONObject object = super.getJson();

        object.put("type", this.getClass().getSimpleName());

        return object;
    }
}
