package com.taobao.arthas.manage.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_connection")
public class AppConnectionDo extends BaseDo{
	private static final long serialVersionUID = -7928457568748680648L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = true)
	private String appIp;
	
	@Column(nullable = true)
	private String appStartCmd;
	
	@Column(nullable = true)
	private Long lastConnectionTime;

	public String getAppIp() {
		return appIp;
	}

	public void setAppIp(String appIp) {
		this.appIp = appIp;
	}

	public String getAppStartCmd() {
		return appStartCmd;
	}

	public void setAppStartCmd(String appStartCmd) {
		this.appStartCmd = appStartCmd;
	}

	public Long getLastConnectionTime() {
		return lastConnectionTime;
	}

	public void setLastConnectionTime(Long lastConnectionTime) {
		this.lastConnectionTime = lastConnectionTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
