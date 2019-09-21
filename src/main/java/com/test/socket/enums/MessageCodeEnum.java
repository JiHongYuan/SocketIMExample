package com.test.socket.enums;

/**
 * @author jihongyuan
 * @date 2019/9/19 14:43
 */
public enum MessageCodeEnum {
    /**
     * 接收
     */
    RECEIVE(1),

    /**
     * 发送
     */
    SEND(2);

    private Integer value;

    MessageCodeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
