package com.taobao.arthas.manage.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_client")
public class AppClientDo extends BaseDo {
	private static final long serialVersionUID = -7928457568748680648L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = true)
	private String appIp;

	@Column(nullable = true)
	private Integer pid;

	@Column(nullable = true, length = 1024)
	private String appStartCmd;

	/**
	 * 保持连接使用的telnet 服务的端口
	 */
	@Column(nullable = true)
	private Integer connTelnetPort;
	
	/**
	 * 接收manage server发送的命令的telnet服务的端口
	 */
	@Column(nullable = true)
	private Integer cmdTelnetPort;
	/**
	 * 接收manage server发送的命令的telnet服务的端口
	 */
	@Column(nullable = true)
	private Boolean isAttached;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppIp() {
		return appIp;
	}

	public void setAppIp(String appIp) {
		this.appIp = appIp;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getAppStartCmd() {
		return appStartCmd;
	}

	public void setAppStartCmd(String appStartCmd) {
		this.appStartCmd = appStartCmd;
	}

	public Integer getConnTelnetPort() {
		return connTelnetPort;
	}

	public void setConnTelnetPort(Integer connTelnetPort) {
		this.connTelnetPort = connTelnetPort;
	}

	public Integer getCmdTelnetPort() {
		return cmdTelnetPort;
	}

	public void setCmdTelnetPort(Integer cmdTelnetPort) {
		this.cmdTelnetPort = cmdTelnetPort;
	}

	public Boolean getIsAttached() {
		return isAttached;
	}

	public void setIsAttached(Boolean isAttached) {
		this.isAttached = isAttached;
	}
	

}
