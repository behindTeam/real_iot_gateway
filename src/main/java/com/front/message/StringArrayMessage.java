/**
 * StringArrayMessage 클래스는 Message 클래스를 상속받아 String 배열을 페이로드로 갖는 메시지를 나타냅니다.
 */
package com.front.message;

public class StringArrayMessage extends Message {
    String[] payload;

    /**
     * StringArrayMessage 클래스의 생성자
     *
     * @param payload String 배열을 포함한 메시지 페이로드
     */
    public StringArrayMessage(String[] payload) {
        this.payload = payload;
    }

    /**
     * 메시지의 페이로드에 포함된 String 배열을 반환하는 메서드
     *
     * @return String 배열을 포함한 페이로드
     */
    public String[] getPayload() {
        return payload;
    }
}
