package com.taobao.arthas.manage.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.taobao.arthas.manage.client.ICommandSender;
import com.taobao.arthas.manage.client.TelnetCommandSender;
import com.taobao.arthas.manage.constants.enums.TaskStatusEnum;
import com.taobao.arthas.manage.dao.AppClientDao;
import com.taobao.arthas.manage.dao.TaskDao;
import com.taobao.arthas.manage.dao.domain.AppClientDo;
import com.taobao.arthas.manage.dao.domain.TaskDo;

@Component
public class TaskService {

	@Resource
	private TaskDao taskDao;
	@Resource
	private AppClientDao appClientDao;

	/**
	 * 执行appId上所有待执行的任务
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 5 Aug 2019 16:46:53
	 */
	public void asynchDoTaskByAppId(Long appClientId) {
		//执行所有此appId上的带执行的任务
		List<TaskDo> taskList = taskDao.getByAppIdAndStatus(appClientId, TaskStatusEnum.ATTACHING.getCode());
		if(!CollectionUtils.isEmpty(taskList)) {
			for(TaskDo taskDo : taskList) {
				asynchDoTask(taskDo.getId());
			}
		}
	}
	
	/**
	 * 根据taskId，执行任务
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 5 Aug 2019 16:46:53
	 */
	public void asynchDoTask(final Long taskId) {
		final TaskDo taskDo = taskDao.getOne(taskId);
		final AppClientDo appClientDo = appClientDao.getOne(taskDo.getAppClientId());

		// 更新任务状态为执行中
		taskDao.updateStatus(taskId, TaskStatusEnum.CMD_DONING.getCode());
		// 异步执行任务
		new Thread(
				new Runnable() {

					@Override
					public void run() {
						// 发送命令行
						ICommandSender commandSender = new TelnetCommandSender(appClientDo.getAppIp(), appClientDo.getCmdTelnetPort());
						String cmdResult = commandSender.sendCommand(taskDo.getCmd());

						// 保存任务执行结果
						taskDao.updateCmdResult(taskId, cmdResult);
						
						//更新任务状态为已完成
						taskDao.updateStatus(taskId, TaskStatusEnum.FINISH.getCode());
					}
				}).start();

	}
}
