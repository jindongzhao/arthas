package com.taobao.arthas.manage.controller;


import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.manage.bo.AppConnectionInfoBo;
import com.taobao.arthas.manage.bo.HttpResponseVo;
import com.taobao.arthas.manage.bo.TaskBo;
import com.taobao.arthas.manage.enums.TaskStatusEnum;
import com.taobao.arthas.manage.enums.TaskTypeEnum;
import com.taobao.arthas.manage.manager.AppConnectionManager;
import com.taobao.arthas.manage.manager.BizTaskManager;

/**
 * 管理页面请求 controller
 * @author zhaojindong
 *
 */
@RestController
public class ManageController {
	
	/**
	 * 查看保持连接的所有App
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 20 Jun 2019 14:28:17
	 */
	@PostMapping("/manage/getAliveAppList")
	public String getAliveAppList() {
		List<AppConnectionInfoBo> resultList = AppConnectionManager.getAliveAppList();
		return JSON.toJSONString(HttpResponseVo.success(resultList));
	}
	
	/**
	 * 执行attach
	* @Description 
	* @param appIdListStr appId列表，英文逗号分隔 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 20 Jun 2019 14:28:23
	 */
	@PostMapping("/manage/doAttach")
	public String doAttach(@RequestParam(name="appIdList",required=true) String appIdListStr,
			@RequestParam(name="loginUserId",required=true) Long loginUserId) {
		String[] idArr = appIdListStr.split(",");
		
		//创建总任务
		TaskBo globalTaskBo = new TaskBo();
		String globalTaskId = UUID.randomUUID().toString();
		globalTaskBo.setTaskId(globalTaskId);
		globalTaskBo.setTaskTypeCode(TaskTypeEnum.ATTACH.getCode());
		globalTaskBo.setTaskStatusCode(TaskStatusEnum.INIT.getCode());
		globalTaskBo.setTaskParam(appIdListStr);
		BizTaskManager.addTask(globalTaskBo);
		
		//创建子任务
		for(String appId : idArr) {
			TaskBo subTaskBo = new TaskBo();
			subTaskBo.setTaskId(UUID.randomUUID().toString());
			subTaskBo.setTaskTypeCode(TaskTypeEnum.ATTACH.getCode());
			subTaskBo.setTaskStatusCode(TaskStatusEnum.INIT.getCode());
			subTaskBo.setAppId(appId);
			subTaskBo.setParentTaskId(globalTaskId);
			BizTaskManager.addTask(subTaskBo);
		}
		
		return JSON.toJSONString(HttpResponseVo.success(null));
	}

}
