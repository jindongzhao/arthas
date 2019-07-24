package com.taobao.arthas.common.dto;

/**
 * 注册成功后返回的Dto
 * @author zhaojindong
 *
 */
public class RegisterRespDto extends ManageBaseDto{
	private Long appClientId;

	public Long getAppClientId() {
		return appClientId;
	}

	public void setAppClientId(Long appClientId) {
		this.appClientId = appClientId;
	}
}
