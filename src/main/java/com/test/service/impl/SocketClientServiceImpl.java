package com.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.test.core.ServiceException;
import com.test.model.UserMessage;
import com.test.service.SocketClientService;
import com.test.socket.client.Connection;
import com.test.socket.client.SocketClient;
import com.test.socket.dto.SocketMessageDto;
import com.test.socket.enums.FunctionCodeEnum;
import com.test.socket.enums.MessageCodeEnum;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author jihongyuan
 * @date 2019/9/21 22:05
 */

@Service
public class SocketClientServiceImpl implements SocketClientService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "clientTaskPool")
    private ThreadPoolTaskExecutor clientExecutor;

    @Resource(name = "clientMessageTaskPool")
    private ThreadPoolTaskExecutor messageExecutor;

    /**
     * 全局缓存，用于存储已存在的socket客户端连接
     */
    public static ConcurrentMap<String, SocketClient> existSocketClientMap = new ConcurrentHashMap<>();

    @Override
    public void startClient(String userId) {
        if (existSocketClientMap.containsKey(userId)) {
            throw new ServiceException("该用户已登陆");
        }
        // 异步创建socket
        clientExecutor.execute(() -> {
            com.test.socket.client.Connection client;
            try {
                client = new Connection(InetAddress.getByName("127.0.0.1"), 5055);
                client.setUserId(userId);
            } catch (UnknownHostException e) {
                throw new ServiceException("socket新建失败");
            }
            // 登陆
            SocketMessageDto socketMessageDto = new SocketMessageDto(FunctionCodeEnum.LOGIN.getValue(), userId);
            client.println(socketMessageDto);

            // 初始化
            ScheduledExecutorService clientHeartExecutor = Executors
                    .newSingleThreadScheduledExecutor(r -> new Thread(r, "socket_client_heart_" + r.hashCode()));
            SocketClient socketClient = new SocketClient(client, new ArrayList<>(), clientHeartExecutor);
            existSocketClientMap.put(userId, socketClient);

            // 监听Server消息
            messageExecutor.submit(() -> {
                try {
                    String message;
                    while ((message = client.readLine()) != null) {
                        logger.info("客户端:{}，获得消息：{}", userId, message);
                        SocketMessageDto socketMessage;
                        try {
                            socketMessage = JSONObject.parseObject(message, SocketMessageDto.class);
                        } catch (Exception e) {
                            logger.error("data error");
                            break;
                        }
                        Integer functionCode = socketMessage.getFunctionCode();
                        if (functionCode.equals(FunctionCodeEnum.HEART.getValue())) {
                            // 心跳
                            client.setLastOnTime(socketMessage.getDate());
                        } else if (functionCode.equals(FunctionCodeEnum.MESSAGE.getValue())) {
                            // 消息
                            existSocketClientMap.get(socketMessage.getUserId()).getMessageList().add(
                                    new UserMessage(socketMessage.getSendUserId(), socketMessage.getMessage(), new Date(), MessageCodeEnum.RECEIVE.getValue()));
                        }
                    }
                } catch (Exception e) {
                    logger.error("客户端异常,userId:{},exception：{}", userId, e.getMessage());
                    client.close(new SocketMessageDto(FunctionCodeEnum.CLOSE.getValue(), client.getUserId()));
                    existSocketClientMap.remove(userId);
                }
            });

            // 心跳定时任务
//            clientHeartExecutor.scheduleWithFixedDelay(() -> {
//                try {
//
//                    Date lastOnTime = client.getLastOnTime();
//                    long heartDuration = (new Date()).getTime() - lastOnTime.getTime();
//                    if (heartDuration > 10000) {
//                        //心跳超时,关闭当前线程
//                        logger.error("心跳超时");
//                        throw new Exception("服务端已断开socket");
//                    }
//                    SocketMessageDto heartDto = new SocketMessageDto(FunctionCodeEnum.HEART.getValue(), userId);
//                    client.println(heartDto);
//                } catch (Exception e) {
//                    logger.error("客户端异常,userId:{},exception：{}", userId, e.getMessage());
//                    client.close(new SocketMessageDto(FunctionCodeEnum.CLOSE.getValue(), userId));
//                    existSocketClientMap.remove(userId);
//                    clientHeartExecutor.shutdown();
//                }
//            }, 0, 5, TimeUnit.SECONDS);
        });
    }

    @Override
    public void closeClient(String userId) {
        if (!existSocketClientMap.containsKey(userId)) {
            throw new ServiceException("该用户未登陆，不能关闭");
        }

        SocketClient client = existSocketClientMap.get(userId);
        client.getConnection().close(new SocketMessageDto(FunctionCodeEnum.CLOSE.getValue(), userId));
        client.getClientHeartExecutor().shutdown();
        existSocketClientMap.remove(userId);
    }

    @Override
    public void sendMessage(String userId, String sendUserId, String message) {

        existSocketClientMap.get(userId).getMessageList().add(
                new UserMessage(sendUserId, message, new Date(), MessageCodeEnum.SEND.getValue()));

        SocketMessageDto messageDto = new SocketMessageDto();
        messageDto.setFunctionCode(FunctionCodeEnum.MESSAGE.getValue());
        messageDto.setUserId(userId);
        messageDto.setSendUserId(sendUserId);
        messageDto.setMessage(message);
        existSocketClientMap.get(userId).getConnection().println(messageDto);
    }

    @Override
    public Set<String> getUsers() {
        return existSocketClientMap.keySet();
    }

    @Override
    public List<UserMessage> getMessage(String userId, String talkUserId) {
        // 网上抄的
        return existSocketClientMap.get(userId).getMessageList().stream().filter(userMessage ->
                userMessage.getUserId().equals(talkUserId)).collect(Collectors.toList());
    }
}
