package com.taobao.arthas.core.manage;

import com.taobao.arthas.core.manage.dto.FinishAttachReqDto;
import com.taobao.arthas.core.manage.dto.ManageBaseDto;
import com.taobao.arthas.core.util.ManageRpcUtil;

/**
 * 管理页面功能相关的业务处理类
 * @author zhaojindong
 *
 */
public class ManageBizHandler {
	private final static String URL_BASE = "http://127.0.0.1:8080";
	private final static String URL_FINISH_ATTACH = URL_BASE + "/client/finishAttach";
	
	/**
	 * 通知manage server attach成功
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 22 Jul 2019 21:14:33
	 */
	public static void notifyAttach(int telnetPort) {
		FinishAttachReqDto reqDto = new FinishAttachReqDto();
		reqDto.setTelnetPort(telnetPort);
		ManageRpcUtil.sendManageRequest(URL_FINISH_ATTACH, reqDto, ManageBaseDto.class);
	}
}
