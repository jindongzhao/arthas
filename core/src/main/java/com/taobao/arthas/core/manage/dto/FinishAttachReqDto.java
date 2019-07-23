package com.taobao.arthas.core.manage.dto;

/**
 * 心跳请求参数
 * @author zhaojindong
 *
 */
public class FinishAttachReqDto {
	public Long taskId;
	private Integer telnetPort;
	
	public Integer getTelnetPort() {
		return telnetPort;
	}
	public void setTelnetPort(Integer telnetPort) {
		this.telnetPort = telnetPort;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
}
