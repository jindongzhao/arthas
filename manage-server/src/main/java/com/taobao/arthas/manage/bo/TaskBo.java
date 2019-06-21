package com.taobao.arthas.manage.bo;

import java.awt.List;

public class TaskBo {
	private String taskId;
	private String taskTypeCode;
	private String taskParam;
	private Integer taskStatusCode;
	private String parentTaskId;
	private String appId;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskTypeCode() {
		return taskTypeCode;
	}
	public void setTaskTypeCode(String taskTypeCode) {
		this.taskTypeCode = taskTypeCode;
	}
	public String getTaskParam() {
		return taskParam;
	}
	public void setTaskParam(String taskParam) {
		this.taskParam = taskParam;
	}
	public Integer getTaskStatusCode() {
		return taskStatusCode;
	}
	public void setTaskStatusCode(Integer taskStatusCode) {
		this.taskStatusCode = taskStatusCode;
	}
	public String getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	
}
