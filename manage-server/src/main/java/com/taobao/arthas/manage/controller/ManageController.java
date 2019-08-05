package com.taobao.arthas.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.common.ManageRpcCommandEnum;
import com.taobao.arthas.manage.client.ICommandSender;
import com.taobao.arthas.manage.client.TelnetCommandSender;
import com.taobao.arthas.manage.command.telnet.ManageTelnetClient;
import com.taobao.arthas.manage.common.HttpResponseVo;
import com.taobao.arthas.manage.dao.AppClientDao;
import com.taobao.arthas.manage.dao.OptTaskDao;
import com.taobao.arthas.manage.dao.domain.AppClientDo;
import com.taobao.arthas.manage.vo.AppClusterVo;

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
	@RequestMapping("/manage/getAppClientList")
	public String getAliveAppList() {
		List<AppClientDo> resultList = appClientDao.findAll();
		return JSON.toJSONString(HttpResponseVo.success(resultList));
	}
	
	/**
	 * 查看集群列表
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 20 Jun 2019 14:28:17
	 */
	@RequestMapping("/manage/getAppClusterList")
	public String getAppClusterList() {
		List<AppClusterVo> resultList = new ArrayList<>();
		List<AppClientDo> appList = appClientDao.findAll();
		//TODO 转化
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
	@RequestMapping("/manage/command/attach")
	public String doAttach(@RequestParam(name = "appClientIdList", required = true) String appClientIdListStr,
			@RequestParam(name = "loginUserId", required = true) Long loginUserId) {
		String[] idArr = appClientIdListStr.split(",");

		for(String idStr : idArr) {
			AppClientDo appClientDo = appClientDao.getOne(Long.valueOf(idStr));
			final String targetTelnetIp = appClientDo.getAppIp();
			final int targetTelnetPort = appClientDo.getConnTelnetPort();
			//连接到app的telnet，并发送attach命令
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					ManageTelnetClient.sendCommand(targetTelnetIp, targetTelnetPort, ManageRpcCommandEnum.COMMAND_ATTACH.getCode());
				}
			}
			).start();
		}

		return JSON.toJSONString(HttpResponseVo.success(null));
	}
	

	/**
	 * 执行自定义命令
	 * 
	 * @Description
	 * @param appIdListStr
	 *            appId列表，英文逗号分隔
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 20 Jun 2019 14:28:23
	 */
	@RequestMapping("/manage/command/cmd")
	public String cmd(@RequestParam(name = "appClientIdList", required = true) String appClientIdListStr,
			@RequestParam(name = "cmd", required = true) String cmd) {
		Map<String, String> resultMap = new HashMap<String, String>();
		//查询应用配置信息
		List<Long> idList = convertAppClientIdList(appClientIdListStr);
		for(Long id : idList) {
			AppClientDo appClientDo = appClientDao.getOne(id);
			ICommandSender commandSender = new TelnetCommandSender(appClientDo.getAppIp(),appClientDo.getCmdTelnetPort());
			String response = commandSender.sendCommand(cmd);
			resultMap.put("id-"+id+"cmd-"+cmd, response);
		}
		
		return JSON.toJSONString(HttpResponseVo.success(resultMap));
	}
	
	private List<Long> convertAppClientIdList(String idListStr){
		List<Long> idList = new ArrayList<Long>();
		String[] idArr = idListStr.split(",");

		for(String idStr : idArr) {
			idList.add(Long.valueOf(idStr));
		}
		
		return idList;
	}

}
