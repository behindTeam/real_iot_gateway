/**
 * LongMessage 클래스는 Message 클래스를 상속받아 long 타입의 payload를 갖는 메시지를 나타냅니다.
 */
package com.front.message;

public class LongMessage extends Message {
    long payload;

    /**
     * LongMessage 클래스의 생성자
     *
     * @param payload long 타입의 메시지 페이로드
     */
    public LongMessage(long payload) {
        this.payload = payload;
    }

    /**
     * 메시지의 페이로드 값을 반환하는 메서드
     *
     * @return long 타입의 페이로드 값
     */
    public long getPayload() {
        return payload;
    }
}
