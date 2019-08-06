package com.taobao.arthas.core.manage;

import com.taobao.arthas.common.ManageRpcUtil;
import com.taobao.arthas.common.dto.FinishAttachReqDto;
import com.taobao.arthas.common.dto.ManageBaseDto;
import com.taobao.arthas.core.util.LogUtil;
import com.taobao.middleware.logger.Logger;

/**
 * 管理页面功能相关的业务处理类
 * @author zhaojindong
 *
 */
public class ManageBizHandler {
    private static Logger logger = LogUtil.getArthasLogger();
    
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
		logger.info("发送notifyAttach请求.url:"+URL_FINISH_ATTACH);
		ManageRpcUtil.sendManageRequest(URL_FINISH_ATTACH, reqDto, ManageBaseDto.class);
	}
}
