package com.taobao.arthas.common.dto;

import java.util.List;

public class HeartBeatRespDto {
	private Integer code;
	private List<String> commandList;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public List<String> getCommandList() {
		return commandList;
	}
	public void setCommandList(List<String> commandList) {
		this.commandList = commandList;
	}
	
}
