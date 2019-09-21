package com.test.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 丁许
 * @date 2019-01-25 14:20
 */

@ApiModel("ServerParamVo")
public class ServerParamVo implements Serializable {

	private static final long serialVersionUID = 5267331270045085979L;

	@ApiModelProperty("用户id")
	private String userId;

	@ApiModelProperty("发送消息")
	private String message;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
