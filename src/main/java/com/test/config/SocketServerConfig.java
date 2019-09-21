package com.test.config;

import com.test.socket.server.SocketServer;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 丁许
 * @date 2019-01-24 22:24
 */
@Configuration
public class SocketServerConfig {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public SocketServer socketServer() {
        SocketServer socketServer = new SocketServer(5055);
        socketServer.start();
        logger.info("socket服务器已启动");
        return socketServer;
    }
}
