package com.taobao.arthas.manage.enums;

public enum TaskStatusEnum {
	INIT(0,"初始化状态"),
	DOING(1,"执行中"),
	DONE(2,"执行完成"),
	FAIL(3,"执行失败");
	
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
