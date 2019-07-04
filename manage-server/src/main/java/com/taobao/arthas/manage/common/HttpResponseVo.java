package com.taobao.arthas.manage.common;

import com.taobao.arthas.manage.constants.enums.ResponseResultEnum;

public class HttpResponseVo {
	private Integer code;
	private String message;
	private Object result;
	
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
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	public static HttpResponseVo success(Object obj) {
		HttpResponseVo responseVo = new HttpResponseVo();
		responseVo.setCode(ResponseResultEnum.SUCCESS.getCode());
		responseVo.setMessage(ResponseResultEnum.SUCCESS.getMessage());
		responseVo.setResult(obj);
		return responseVo;
	}
	
	public static HttpResponseVo fail() {
		HttpResponseVo responseVo = new HttpResponseVo();
		responseVo.setCode(ResponseResultEnum.FAIL.getCode());
		responseVo.setMessage(ResponseResultEnum.FAIL.getMessage());
		return responseVo;
	}
}
