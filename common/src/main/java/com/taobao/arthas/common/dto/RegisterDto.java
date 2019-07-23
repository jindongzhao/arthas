package com.taobao.arthas.common.dto;

/**
 * 注册Dto
 * @author zhaojindong
 *
 */
public class RegisterDto {
	private String appIp;
	private Integer pid;
	/**
	 * 启动命令行
	 */
	private String appStartCmd;
	/**
	 * 保持连接使用的telnet 服务的端口
	 */
	private Integer connTelnetPort;
	
	/**
	 * 接收manage server发送的命令的telnet服务的端口
	 */
	private Integer cmdTelnetPort;
	
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
	
}
