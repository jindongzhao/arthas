package com.taobao.arthas.common.dto;

/**
 * 心跳请求参数
 * @author zhaojindong
 *
 */
public class HeartBeatReqDto {
	public String appIp;
	public String appStartCmd;
	
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
	
}
