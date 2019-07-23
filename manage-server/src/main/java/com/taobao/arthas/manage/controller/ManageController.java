package com.taobao.arthas.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.common.ManageRpcCommandEnum;
import com.taobao.arthas.manage.command.telnet.ManageTelnetClient;
import com.taobao.arthas.manage.common.HttpResponseVo;
import com.taobao.arthas.manage.constants.enums.TaskStatusEnum;
import com.taobao.arthas.manage.constants.enums.TaskTypeEnum;
import com.taobao.arthas.manage.dao.AppClientDao;
import com.taobao.arthas.manage.dao.OptTaskDao;
import com.taobao.arthas.manage.dao.domain.AppClientDo;
import com.taobao.arthas.manage.dao.domain.OptTaskDo;

/**
 * 管理页面请求 controller
 * 
 * @author zhaojindong
 *
 */
@RestController
public class ManageController {

	@Resource
	private AppClientDao appClientDao;
	@Resource
	private OptTaskDao optTaskDao;

	/**
	 * 查看保持连接的所有App
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 20 Jun 2019 14:28:17
	 */
	@PostMapping("/manage/getAppClientList")
	public String getAliveAppList() {
		List<AppClientDo> resultList = appClientDao.findAll();
		return JSON.toJSONString(HttpResponseVo.success(resultList));
	}

	/**
	 * 执行attach
	 * 
	 * @Description
	 * @param appIdListStr
	 *            appId列表，英文逗号分隔
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 20 Jun 2019 14:28:23
	 */
	@PostMapping("/manage/command/attach")
	public String doAttach(@RequestParam(name = "appClientIdList", required = true) String appClientIdListStr,
			@RequestParam(name = "loginUserId", required = true) Long loginUserId) {
		String[] idArr = appClientIdListStr.split(",");

		for(String idStr : idArr) {
			AppClientDo appClientDo = appClientDao.getOne(Long.valueOf(idStr));
			String targetTelnetIp = appClientDo.getAppIp();
			int targetTelnetPort = appClientDo.getConnTelnetPort();
			//连接到app的telnet，并发送attach命令
			ManageTelnetClient.sendCommand(targetTelnetIp, targetTelnetPort, ManageRpcCommandEnum.COMMAND_ATTACH.getCode());
		}

		return JSON.toJSONString(HttpResponseVo.success(null));
	}

}
