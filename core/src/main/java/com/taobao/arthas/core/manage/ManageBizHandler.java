package com.taobao.arthas.core.manage;

import com.taobao.arthas.common.ManageRpcUtil;
import com.taobao.arthas.common.dto.FinishAttachReqDto;
import com.taobao.arthas.common.dto.ManageBaseDto;

/**
 * 管理页面功能相关的业务处理类
 * @author zhaojindong
 *
 */
public class ManageBizHandler {
	private final static String URL_BASE = "http://127.0.0.1:9009";
	private final static String URL_FINISH_ATTACH = URL_BASE + "/client/finishAttach";
	
	/**
	 * 通知manage server attach成功
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 22 Jul 2019 21:14:33
	 */
	public static void notifyAttach(Long appClientId) {
		FinishAttachReqDto reqDto = new FinishAttachReqDto();
		reqDto.setAppClientId(appClientId);
		ManageRpcUtil.sendManageRequest(URL_FINISH_ATTACH, reqDto, ManageBaseDto.class);
	}
}
