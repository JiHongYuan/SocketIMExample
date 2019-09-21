package com.test.socket.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.core.ServiceException;
import com.test.model.UserMessage;
import com.test.socket.dto.SocketMessageDto;
import com.test.socket.enums.FunctionCodeEnum;
import com.test.socket.enums.MessageCodeEnum;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.test.socket.server.SocketServer.existSocketMap;
import static com.test.socket.server.SocketServer.userMessageMap;

/**
 * @author jihongyuan
 * @date 2019/9/10 21:10
 */
public class ConnectionThread extends Thread {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 封装用户连接信息 socket
     */
    private Connection connection;

    private boolean isRunning;

    public ConnectionThread(Connection connection) {
        this.connection = connection;
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            /**
             *  判断 accept socket 是否关闭
             *  和客户端socket关闭无关！！！
             * */
            if (connection.isClosed()) {
                isRunning = false;
                break;
            }

            BufferedReader in;
            try {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    SocketMessageDto messageDto;
                    try {
                        messageDto = JSONObject.parseObject(message, SocketMessageDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    logger.info("服务端收到消息：{}", JSONObject.toJSONString(messageDto));
                    Integer functionCode = messageDto.getFunctionCode();

                    if (functionCode.equals(FunctionCodeEnum.LOGIN.getValue())) {
                        // 登陆
                        String userId = messageDto.getUserId();
                        connection.setLogin(true);
                        connection.setUserId(userId);

                        if (existSocketMap.containsKey(userId)) {
                            // 关闭当前socket
                            connection.close();
                        }
                        existSocketMap.put(userId, connection);
                        // 初始化用户记录
                        userMessageMap.put(userId, Collections.synchronizedList(new ArrayList<>()));

                    } else if (functionCode.equals(FunctionCodeEnum.MESSAGE.getValue())) {
                        // 消息
                        // 用户登录后一定存在 不需要在判断key存不存在

                        // A -> B  A - B,message,date,2
                        userMessageMap.get(messageDto.getUserId()).add(
                                new UserMessage(messageDto.getSendUserId(), messageDto.getMessage(), new Date(), MessageCodeEnum.SEND.getValue()));
                        // B <- A  B - A,message,date,1
                        userMessageMap.get(messageDto.getSendUserId()).add(
                                new UserMessage(messageDto.getUserId(), messageDto.getMessage(), new Date(), MessageCodeEnum.RECEIVE.getValue()));

                        // A.socket( uId = A, sId = B) -> message -> B.socket( uId = B, sId = A)
                        // message userId和sendUserId 要倒过来
                        String swap = messageDto.getUserId();
                        messageDto.setUserId(messageDto.getSendUserId());
                        messageDto.setSendUserId(swap);
                        connection.println(JSONObject.toJSONString(messageDto));
                    } else if (functionCode.equals(FunctionCodeEnum.HEART.getValue())) {
                        // 心跳
                        SocketMessageDto heart = new SocketMessageDto();
                        heart.setFunctionCode(FunctionCodeEnum.HEART.getValue());
                        connection.println(JSONObject.toJSONString(heart));
                    } else if (functionCode.equals(FunctionCodeEnum.CLOSE.getValue())) {
                        // 关闭
                        stopRunning();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                // 当客户端socket断开会抛异常，此处才是回收线程的关键所在。
                stopRunning();
            }
        }
    }


    public void stopRunning() {
        if (connection.isLogin()) {
            //登陆

            // 清除活跃的socket
            existSocketMap.remove(connection.getUserId());
            // 清除聊天记录
            userMessageMap.remove(connection.getUserId());
        } else {
            //没有验证
        }
        isRunning = false;
        try {
            connection.close();
        } catch (IOException e) {
            //
        }
    }

    /**
     * 统一捕捉异常进行处理
     */
    public void println(String s) {
        try {
            connection.println(s);
        } catch (ServiceException e) {
            stopRunning();
        }
    }

}
