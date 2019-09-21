package com.test.socket.client;

import com.test.model.UserMessage;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author jihongyuan
 * @date 2019/9/21 21:28
 */
public class SocketClient {

    private Connection connection;

    /**
     *  保存消息
     * */
    private List<UserMessage> messageList;

    private ScheduledExecutorService clientHeartExecutor;

    public SocketClient() {
    }

    public SocketClient(Connection connection, List<UserMessage> messageList, ScheduledExecutorService clientHeartExecutor) {
        this.connection = connection;
        this.messageList = messageList;
        this.clientHeartExecutor = clientHeartExecutor;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public List<UserMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<UserMessage> messageList) {
        this.messageList = messageList;
    }

    public ScheduledExecutorService getClientHeartExecutor() {
        return clientHeartExecutor;
    }

    public void setClientHeartExecutor(ScheduledExecutorService clientHeartExecutor) {
        this.clientHeartExecutor = clientHeartExecutor;
    }

    public static void main(String[] args) {

    }
}
