package com.taobao.arthas.manage.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.common.dto.HeartBeatRespDto;
import com.taobao.arthas.manage.bo.AppConnectionInfoBo;
import com.taobao.arthas.manage.bo.TaskBo;
import com.taobao.arthas.manage.enums.ResponseResultEnum;
import com.taobao.arthas.manage.enums.TaskStatusEnum;
import com.taobao.arthas.manage.enums.TaskTypeEnum;
import com.taobao.arthas.manage.manager.AppConnectionManager;
import com.taobao.arthas.manage.manager.BizTaskManager;

/**
 * 心跳维持 controller
 * @author zhaojindong
 *
 */
@RestController
public class HeartBeatController {
	
	/**
	 * 客户端发送请求告诉服务端，自己存活着
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 14 Jun 2019 14:04:43
	 */
	@PostMapping("/heartBeat/isAlive")
	public String isAlive(@RequestParam(name="params",required=true) String params) {
		AppConnectionInfoBo appInfo = JSON.parseObject(params, AppConnectionInfoBo.class);
		AppConnectionManager.addOrUpdate(appInfo);
		
		HeartBeatRespDto heartBeatResp = new HeartBeatRespDto();
		heartBeatResp.setCode(ResponseResultEnum.SUCCESS.getCode());
		
		//查询需要执行的命令
		List<String> commandList = new ArrayList<>();
		String appId = AppConnectionManager.getAppId(appInfo);
		List<TaskBo> taskList = BizTaskManager.getTaskListByAppId(appId);
		if(taskList != null) {
			for(TaskBo taskBo : taskList) {
				//初始化状态的task
				if(TaskStatusEnum.INIT.getCode().equals(taskBo.getTaskStatusCode())) {
					//attach命令
					if(TaskTypeEnum.ATTACH.getCode().equals(taskBo.getTaskTypeCode())){
						commandList.add("attach");
					}
					
					//更新命令状态为执行中
					BizTaskManager.updateTaskStatus(taskBo.getTaskId(), TaskStatusEnum.DOING);
				}
			}
		}
		
		heartBeatResp.setCommandList(commandList);
		
		return JSON.toJSONString(heartBeatResp);
	}

}
