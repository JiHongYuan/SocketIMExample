package com.test.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import static com.test.socket.server.SocketServer.existConnectionThreadList;

/**
 * @author jihongyuan
 * @date 2019/9/10 21:01
 */
public class SocketListeningThread extends Thread {
    /**
     * 包装的SocketServer
     */
    private SocketServer socketServer;

    private boolean isRunning;

    /**
     * @param socketServer 使用这个包装类，而不是ServerSocket
     */
    public SocketListeningThread(SocketServer socketServer) {
        this.socketServer = socketServer;
        isRunning = true;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = socketServer.getServerSocket();
        while (isRunning) {

            if (serverSocket.isClosed()) {
                isRunning = false;
                break;
            }

            try {
                Socket socket = serverSocket.accept();
                // 判断分配线程的数量
                ///


                // 分配线程
                Date date = new Date();
                Connection connection = new Connection(socket, date, date);

                ConnectionThread connectionThread = new ConnectionThread(connection);
                existConnectionThreadList.add(connectionThread);
                connectionThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
