package com.taobao.arthas.manage.vo;

import java.util.List;

public class TaskVo {
	private Long taskId;
	private String cmd;
	private String appIp;
	private String cmdResult;
	private Integer status;
	private List<TaskVo> childList;
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getAppIp() {
		return appIp;
	}
	public void setAppIp(String appIp) {
		this.appIp = appIp;
	}
	public String getCmdResult() {
		return cmdResult;
	}
	public void setCmdResult(String cmdResult) {
		this.cmdResult = cmdResult;
	}
	public List<TaskVo> getChildList() {
		return childList;
	}
	public void setChildList(List<TaskVo> childList) {
		this.childList = childList;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
