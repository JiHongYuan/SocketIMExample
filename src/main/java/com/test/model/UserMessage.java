package com.test.model;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jihongyuan
 * @date 2019/9/18 21:37
 */
public class UserMessage {

    /**
     *  1 接收 2 发送
     * */
    private Integer state;

    private String userId;

    private String message;

    private Date date;

    public UserMessage(String userId, String message, Date date, Integer state) {
        this.userId = userId;
        this.message = message;
        this.date = date;
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
