package com.taobao.arthas.manage.constants.enums;

/**
 * 任务状态
 * @author zhaojindong
 *
 */
public enum TaskStatusEnum {
	INIT(0,"初始化"),
	ATTACHING(1,"attach中"),
	CMD_DONING(2,"cmd执行中"),
	FINISH(3,"执行完成");
	
	TaskStatusEnum(Integer code,String message){
		this.code = code;
		this.message = message;
	}
	
	private Integer code;
	private String message;

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
	
}
