package com.test.socket.server;

import com.test.core.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author jihongyuan
 * @date 2019/9/10 21:12
 */
public class Connection {

    /**
     * 当前的socket连接实例
     */
    private Socket socket;

    /**
     * 当前连接是否登陆
     */
    private boolean isLogin;

    /**
     * 存储当前的user信息
     */
    private String userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后一次更新时间，用于判断心跳
     */
    private Date lastOnTime;

    public void println(String message) throws ServiceException {
        int count = 0;
        PrintWriter writer;
        do {
            try {
                writer = new PrintWriter(new OutputStreamWriter(
                        socket.getOutputStream()), true);
                writer.println(message);
                break;
            } catch (IOException e) {
                if (count++ >= 3) {
                    throw new ServiceException("重试超过最大次数");
                }
            }
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (count < 3);
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void close() throws IOException {
        socket.close();
    }


    public Connection(Socket socket, Date createTime, Date lastOnTime) {
        this.socket = socket;
        this.createTime = createTime;
        this.lastOnTime = lastOnTime;
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastOnTime() {
        return lastOnTime;
    }

    public void setLastOnTime(Date lastOnTime) {
        this.lastOnTime = lastOnTime;
    }
}
