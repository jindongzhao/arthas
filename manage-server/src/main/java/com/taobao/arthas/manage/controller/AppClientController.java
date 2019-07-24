package com.taobao.arthas.manage.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.arthas.common.ManageRespsCodeEnum;
import com.taobao.arthas.common.ManageRpcUtil;
import com.taobao.arthas.common.dto.FinishAttachReqDto;
import com.taobao.arthas.common.dto.ManageBaseDto;
import com.taobao.arthas.common.dto.RegisterReqDto;
import com.taobao.arthas.common.dto.RegisterRespDto;
import com.taobao.arthas.manage.dao.AppClientDao;
import com.taobao.arthas.manage.dao.domain.AppClientDo;

/**
 * 客户端 controller
 * -通知attach结果等
 * @author zhaojindong
 *
 */
@RestController
public class AppClientController {

	@Resource
	private AppClientDao appClientDao;
	
	/**
	 * 注册
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 20 Jun 2019 14:28:17
	 */
	@PostMapping("/client/register")
	public String register(@RequestParam(name = "params", required = true) String params) {
		RegisterReqDto reqDto = ManageRpcUtil.deserializeReqParam(params, RegisterReqDto.class);
		AppClientDo registerDo = new AppClientDo();
		registerDo.setAppIp(reqDto.getAppIp());
		registerDo.setAppStartCmd(reqDto.getAppStartCmd());
		registerDo.setCmdTelnetPort(reqDto.getCmdTelnetPort());
		registerDo.setConnTelnetPort(reqDto.getConnTelnetPort());
		registerDo.setPid(reqDto.getPid());
		registerDo.setIsAttached(false);
		appClientDao.save(registerDo);

		RegisterRespDto respDto = new RegisterRespDto();
		respDto.setResultCode(ManageRespsCodeEnum.SUCCESS.getCode());
		respDto.setAppClientId(registerDo.getId());
		return ManageRpcUtil.serializeRspsResult(respDto);
	}

	/**
	 * 完成attach
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 20 Jun 2019 14:28:17
	 */
	@PostMapping("/client/finishAttach")
	public String finishAttach(@RequestParam(name = "params", required = true) String params) {
		FinishAttachReqDto reqDto = ManageRpcUtil.deserializeReqParam(params, FinishAttachReqDto.class);
		Long appClientId = reqDto.getAppClientId();
		//更新客户端状态为已连接
		appClientDao.updateAttachStatus(appClientId, true);
		
		ManageBaseDto baseDto = new ManageBaseDto();
		baseDto.setResultCode(ManageRespsCodeEnum.SUCCESS.getCode());
		return ManageRpcUtil.serializeRspsResult(baseDto);
	}

}
