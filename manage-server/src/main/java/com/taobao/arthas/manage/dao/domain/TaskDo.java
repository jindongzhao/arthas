package com.taobao.arthas.manage.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 任务表
 * @author zhaojindong
 *
 */
@Entity
@Table(name = "task")
public class TaskDo extends BaseDo {
	private static final long serialVersionUID = -7928457568748680648L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 父任务id
	 */
	@Column(nullable = true)
	private Long parentId;
	
	/**
	 * 执行命令的user id
	 */
	@Column(nullable = true)
	private Long userId;
	
	/**
	 * app client id
	 */
	@Column(nullable = true)
	private Long appClientId;
	
	/**
	 * 需要执行的arthas命令
	 */
	@Column(nullable = true)
	private String cmd;

	/**
	 * 0-初始化
	 * 1-attach中
	 * 2-cmd执行中
	 * 3-执行完成
	 */
	@Column(nullable = true)
	private Integer status;
	
	/**
	 * 命令执行结果
	 */
	@Column(nullable = true)
	private String cmdResult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAppClientId() {
		return appClientId;
	}

	public void setAppClientId(Long appClientId) {
		this.appClientId = appClientId;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getCmdResult() {
		return cmdResult;
	}

	public void setCmdResult(String cmdResult) {
		this.cmdResult = cmdResult;
	}

	
}
