package com.taobao.arthas.manage.enums;

public enum TaskTypeEnum {
	ATTACH("attach","attach到目标jvm进程");
	
	TaskTypeEnum(String code,String message){
		this.code = code;
		this.message = message;
	}
	
	private String code;
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
	
}
