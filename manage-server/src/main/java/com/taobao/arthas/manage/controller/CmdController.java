package com.taobao.arthas.manage.controller;

import java.util.ArrayList;
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
import com.taobao.arthas.manage.vo.TaskVo;

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
		parentTaskDo.setUserId(loginUserId);
		taskDao.save(parentTaskDo);
		
		//保存子任务
		for(Long appId : appIdList) {
			TaskDo taskDo = new TaskDo();
			taskDo.setParentId(parentTaskDo.getId());
			taskDo.setAppClientId(appId);
			
			//查询app client 的attach 状态
			AppClientDo appClientDo = appClientDao.getOne(appId);
			Integer taskStatus = TaskStatusEnum.INIT.getCode();
			if(Boolean.TRUE.equals(appClientDo.getIsAttached())) {
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
	
	/**
	 * 查询命令执行结果
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 5 Aug 2019 14:28:24
	 */
	@RequestMapping("/manage/cmd/getCmdResult")
	public String getCmdResult(@RequestParam(name = "taskId", required = true) Long taskId) {
		TaskVo taskVo = new TaskVo();
		
		TaskDo parentTaskDo = taskDao.getOne(taskId);
		taskVo.setTaskId(parentTaskDo.getId());
		taskVo.setCmd(parentTaskDo.getCmd());
		taskVo.setStatus(parentTaskDo.getStatus());
		
		List<TaskDo> childTaskList = taskDao.getByParentId(parentTaskDo.getId());
		if(childTaskList != null) {
			List<TaskVo> taskVoList = new ArrayList<TaskVo>();
			for(TaskDo childTaskDo : childTaskList) {
				TaskVo childTaskVo = new TaskVo();
				AppClientDo appClientDo = appClientDao.getOne(childTaskDo.getAppClientId());
				childTaskVo.setAppIp(appClientDo.getAppIp());
				childTaskVo.setCmd(childTaskDo.getCmd());
				childTaskVo.setCmdResult(childTaskDo.getCmdResult());
				childTaskVo.setTaskId(childTaskDo.getId());
				childTaskVo.setStatus(childTaskDo.getStatus());
				taskVoList.add(childTaskVo);
			}
			taskVo.setChildList(taskVoList);
		}
		
		return JSON.toJSONString(HttpResponseVo.success(taskVo));
	}

}
