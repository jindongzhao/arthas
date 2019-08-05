package com.taobao.arthas.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.manage.common.HttpResponseVo;
import com.taobao.arthas.manage.constants.enums.TaskStatusEnum;
import com.taobao.arthas.manage.dao.AppClientDao;
import com.taobao.arthas.manage.dao.TaskDao;
import com.taobao.arthas.manage.dao.domain.AppClientDo;
import com.taobao.arthas.manage.dao.domain.TaskDo;
import com.taobao.arthas.manage.service.AppClientService;
import com.taobao.arthas.manage.service.TaskService;

/**
 * 执行具体命令的controller
 * 
 * @author zhaojindong
 *
 */
@RestController
public class CmdController {
	@Resource
	private AppClientDao appClientDao;
	@Resource
	private TaskDao taskDao;
	@Resource
	private TaskService taskService;
	@Resource
	private AppClientService appClientService;
	
	/**
	 * 对选中的app，执行命令
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 5 Aug 2019 14:28:24
	 */
	@RequestMapping("/manage/cmd/doCmdByApp")
	public String doCmdByApp(@RequestParam(name = "appIdList", required = true) List<Long> appIdList,
			@RequestParam(name = "loginUserId", required = true) Long loginUserId,
			@RequestParam(name = "cmd", required = true) String cmd) {
		//保存总任务
		TaskDo parentTaskDo = new TaskDo();
		parentTaskDo.setCmd(cmd);
		taskDao.save(parentTaskDo);
		
		//保存子任务
		for(Long appId : appIdList) {
			TaskDo taskDo = new TaskDo();
			taskDo.setParentId(parentTaskDo.getId());
			taskDo.setAppClientId(appId);
			
			//查询app client 的attach 状态
			AppClientDo appClientDo = appClientDao.getOne(appId);
			Integer taskStatus = TaskStatusEnum.INIT.getCode();
			if(Boolean.FALSE.equals(appClientDo.getIsAttached())) {
				taskStatus = TaskStatusEnum.CMD_DONING.getCode();
			}else {
				//attach到目标进程
				appClientService.asynchDoAttach(appClientDo.getAppIp(), appClientDo.getConnTelnetPort());
				taskStatus = TaskStatusEnum.ATTACHING.getCode();
			}
			
			taskDo.setStatus(taskStatus);
			taskDo.setCmd(cmd);
			taskDo.setUserId(loginUserId);
			taskDao.save(taskDo);
			
			//可以立即执行的task
			if(TaskStatusEnum.CMD_DONING.getCode().equals(taskDo.getStatus())) {
				taskService.asynchDoTask(taskDo.getId());
			}
		}
		
		//返回总任务的id到页面
		return JSON.toJSONString(HttpResponseVo.success(parentTaskDo.getId()));
	}

}
