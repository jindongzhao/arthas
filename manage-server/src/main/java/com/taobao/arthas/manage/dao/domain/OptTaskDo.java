package com.taobao.arthas.manage.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "opt_task")
public class OptTaskDo extends BaseDo{
	private static final long serialVersionUID = -7928457568748680648L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = true)
	private String taskTypeCode;
	
	@Column(nullable = true)
	private String taskParam;
	
	@Column(nullable = true)
	private Integer taskStatusCode;
	
	@Column(nullable = true)
	private Long parentTaskId;
	
	@Column(nullable = true)
	private String appId;

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

	public Long getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(Long parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
