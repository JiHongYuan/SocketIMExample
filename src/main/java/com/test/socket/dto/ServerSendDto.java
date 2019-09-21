package com.test.socket.dto;

import java.io.Serializable;

/**
 * @author 丁许
 * @date 2019-01-24 15:13
 */
public class ServerSendDto implements Serializable {

	private static final long serialVersionUID = -7453297551797390215L;

	/**
	 * 状态码 20000 成功，否则有errorMessage
	 */
	private Integer statusCode;

	private String message;

	/**
	 * 功能码
	 */
	private Integer functionCode;

	/**
	 * 错误消息
	 */
	private String errorMessage;

	public ServerSendDto(Integer statusCode, String message, Integer functionCode) {
		this.statusCode = statusCode;
		this.message = message;
		this.functionCode = functionCode;
	}

	public ServerSendDto() {
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(Integer functionCode) {
		this.functionCode = functionCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
