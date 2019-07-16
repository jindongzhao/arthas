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
	
	/**
	 * 任务code
	 */
	@Column(nullable = true)
	private String taskTypeCode;
	
	/**
	 * 任务参数
	 */
	@Column(nullable = true)
	private String taskParam;
	
	/**
	 * 任务状态code
	 */
	@Column(nullable = true)
	private Integer taskStatusCode;
	
	/**
	 * 父任务id
	 */
	@Column(nullable = true)
	private Long parentTaskId;
	
	/**
	 * 需要执行任务的app id
	 */
	@Column(nullable = true)
	private Long appConnectionId;

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

	public Long getAppConnectionId() {
		return appConnectionId;
	}

	public void setAppConnectionId(Long appConnectionId) {
		this.appConnectionId = appConnectionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
