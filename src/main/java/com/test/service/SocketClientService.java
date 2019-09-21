package com.test.service;

import com.test.model.UserMessage;

import java.util.List;
import java.util.Set;

/**
 * @author jihongyuan
 * @date 2019/9/21 21:56
 */
public interface SocketClientService {
    /**
     * 启动一个客户端
     *
     * @param userId user id
     */
    void startClient(String userId);

    /**
     * 关闭一个客户端
     *
     * @param userId user id
     */
    void closeClient(String userId);

    /**
     * 发送一条消息
     *
     * @param userId     user id
     * @param sendUserId send user id
     * @param message    消息体
     */
    void sendMessage(String userId, String sendUserId, String message);

    /**
     * 获取所有在线用户
     *
     * @return Set<String>
     */
    Set<String> getUsers();

    /**
     * 获取用户聊天记录
     *
     * @param userId     user id
     * @param talkUserId talk user id
     * @return List<UserMessage>
     */
    List<UserMessage> getMessage(String userId, String talkUserId);
}
