package com.test.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 丁许
 * @date 2019-01-25 9:48
 */

@ApiModel("ClientParamVo")
public class ClientParamVo implements Serializable {

	private static final long serialVersionUID = 2822768619906469920L;

	@ApiModelProperty("用户id")
	private String userId;

	@ApiModelProperty("发送用户id")
	private String sendUserId;

	@ApiModelProperty("发送消息")
	private String message;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
