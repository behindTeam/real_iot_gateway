/**
 * BooleanMessage 클래스는 Message 클래스를 상속받아 boolean 타입의 payload를 갖는 메시지입니다.
 */
package com.front.message;

public class BooleanMessage extends Message {
    boolean payload;

    /**
     * BooleanMessage 클래스의 생성자
     *
     * @param payload boolean 타입의 메시지 payload
     */
    public BooleanMessage(boolean payload) {
        this.payload = payload;
    }

    /**
     * 메시지의 payload 값을 반환하는 메서드
     *
     * @return boolean 타입의 payload 값
     */
    public boolean getPayload() {
        return payload;
    }
}
