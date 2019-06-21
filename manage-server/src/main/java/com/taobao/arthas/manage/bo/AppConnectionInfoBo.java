package com.taobao.arthas.manage.bo;

public class AppConnectionInfoBo {
	private String id;
	private String appIp;
	private String appStartCmd;
	private Long lastConnectionTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	
	
}
