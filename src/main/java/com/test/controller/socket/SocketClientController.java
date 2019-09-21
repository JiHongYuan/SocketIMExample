package com.test.controller.socket;

import com.test.core.ResponseEntity;
import com.test.core.ServiceException;
import com.test.model.ClientParamVo;
import com.test.model.UserMessage;
import com.test.service.SocketClientService;
import com.test.socket.client.SocketClient;
import com.test.socket.dto.SocketMessageDto;
import com.test.socket.enums.FunctionCodeEnum;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author jihongyuan
 * @date 2019/9/18 11:25
 */

@RestController
@RequestMapping("/socket-client")
public class SocketClientController {

    @Resource
    private SocketClientService socketClientService;

    @ApiOperation(value = "开启一个socket客户端")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
    })
    @PostMapping("/start")
    public ResponseEntity<?> startClient(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("参数不全");
        }

        socketClientService.startClient(userId);
        return ResponseEntity.success();
    }

    @ApiOperation(value = "关闭一个socket客户端")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
    })
    @PostMapping("/close")
    public ResponseEntity<?> closeClient(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("参数不全");
        }

        socketClientService.closeClient(userId);
        return ResponseEntity.success();
    }

    @ApiOperation(value = "发送一条消息")
    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody ClientParamVo paramVo) {
        if (StringUtils.isEmpty(paramVo.getUserId()) || StringUtils.isEmpty(paramVo.getMessage()) || StringUtils.isEmpty(paramVo.getSendUserId())) {
            throw new ServiceException("参数不全");
        }

        socketClientService.sendMessage(paramVo.getUserId(), paramVo.getSendUserId(), paramVo.getMessage());
        return ResponseEntity.success();

    }

    @GetMapping("/get-users")
    public ResponseEntity<Set<String>> getUsers() {
        return ResponseEntity.success(socketClientService.getUsers());
    }

    @GetMapping("/get-message")
    public ResponseEntity<List<UserMessage>> getMessage(String userId,String talkUserId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(talkUserId)) {
            throw new ServiceException("参数不全");
        }

        return ResponseEntity.success(socketClientService.getMessage(userId,talkUserId));
    }

}
