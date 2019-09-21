package com.test.socket.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jihongyuan
 * @date 2019/9/18 20:41
 * 用于server_socket 与 client_socket之间传递消息
 */
public class SocketMessageDto implements Serializable {

    private static final long serialVersionUID = 97085384412852967L;

    /**
     * 功能码 0 心跳 1 登陆 2 登出 3 发送消息
     */
    private Integer functionCode;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 发送消息的用户id
     */
    private String sendUserId;

    /**
     * 消息体
     */
    private String message;

    /**
     * 发送时间
     * */
    private Date date;

    public SocketMessageDto() {
        date = new Date();
    }

    public SocketMessageDto(Integer functionCode, String userId) {
        this.functionCode = functionCode;
        this.userId = userId;
        date = new Date();
    }

    public SocketMessageDto(Integer functionCode, String userId, String message) {
        this.functionCode = functionCode;
        this.userId = userId;
        this.message = message;
        date = new Date();
    }

    public SocketMessageDto(Integer functionCode, String userId, String sendUserId, String message) {
        this.functionCode = functionCode;
        this.userId = userId;
        this.sendUserId = sendUserId;
        this.message = message;
        date = new Date();
    }

    public Integer getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(Integer functionCode) {
        this.functionCode = functionCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
