package com.test.controller.socket;

import com.alibaba.fastjson.JSONObject;
import com.test.core.ResponseEntity;
import com.test.core.ServiceException;
import com.test.model.ServerParamVo;
import com.test.socket.dto.ServerSendDto;
import com.test.socket.enums.FunctionCodeEnum;
import com.test.socket.server.Connection;
import com.test.socket.server.SocketServer;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.test.socket.server.SocketServer.existSocketMap;

/**
 * @author jihongyuan
 * @date 2019/9/18 11:25
 */

@RestController
@RequestMapping("/socket-server")
public class SocketServerController {

    private SocketServer socketServer;

    @ApiOperation(value = "获取所有登陆用户")
    @GetMapping("/get-users")
    public ResponseEntity<JSONObject> getLoginUsers() {
        JSONObject result = new JSONObject();
        result.put("total", existSocketMap.keySet().size());
        result.put("dataList", existSocketMap.keySet());
        return ResponseEntity.success(result);
    }

    @ApiOperation(value = "发送一条消息")
    @PostMapping("/send-message")
    public ResponseEntity<JSONObject> sendMessage(@RequestBody ServerParamVo paramVo) {
        if (StringUtils.isEmpty(paramVo.getUserId()) || StringUtils.isEmpty(paramVo.getMessage())) {
            throw new ServiceException("参数不全");
        }
        if (!existSocketMap.containsKey(paramVo.getUserId())) {
            throw new ServiceException("并没有客户端连接");
        }

        Connection connection = existSocketMap.get(paramVo.getUserId());
        ServerSendDto sendDto = new ServerSendDto(20000, paramVo.getMessage(), FunctionCodeEnum.MESSAGE.getValue());
        connection.println(JSONObject.toJSONString(sendDto));

        return ResponseEntity.success();
    }

    @Autowired
    public void setSocketServer(SocketServer socketServer) {
        this.socketServer = socketServer;
    }
}
