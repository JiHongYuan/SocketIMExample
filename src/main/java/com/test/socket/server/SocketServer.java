package com.test.socket.server;

import com.test.model.UserMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jihongyuan
 * @date 2019/9/1020:56
 */
public class SocketServer {

    /**
     * 存储 accept socket处理的线程
     */
    public static List<ConnectionThread> existConnectionThreadList = Collections.synchronizedList(new ArrayList<>());

    /**
     * 存储 用户登陆的socket
     */
    public static ConcurrentMap<String, Connection> existSocketMap = new ConcurrentHashMap<>();

    /**
     * 存储 用户聊天记录
     */
    public static ConcurrentHashMap<String,List<UserMessage>> userMessageMap = new ConcurrentHashMap<>();

    private ServerSocket serverSocket;

    /**
     * server socket的监听线程
     */
    private SocketListeningThread socketListeningThread;


    public SocketServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        socketListeningThread = new SocketListeningThread(this);
        socketListeningThread.start();
    }


    public ServerSocket getServerSocket() {
        return serverSocket;
    }

}
