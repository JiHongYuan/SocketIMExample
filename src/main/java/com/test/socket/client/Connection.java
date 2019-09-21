package com.test.socket.client;

import com.alibaba.fastjson.JSONObject;
import com.test.socket.dto.SocketMessageDto;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

/**
 * @author jihongyuan
 * @date 2019/9/11 12:17
 */
public class Connection {

    private String userId;

    private Socket socket;

    private Date lastOnTime;

    public Connection(InetAddress ip, int port) {
        try {
            socket = new Socket(ip, port);
            socket.setKeepAlive(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(SocketMessageDto message) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(JSONObject.toJSONString(message));

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void println(SocketMessageDto message) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(JSONObject.toJSONString(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() throws Exception {
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return reader.readLine();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    public Date getLastOnTime() {
        return lastOnTime;
    }

    public void setLastOnTime(Date lastOnTime) {
        this.lastOnTime = lastOnTime;
    }

}
