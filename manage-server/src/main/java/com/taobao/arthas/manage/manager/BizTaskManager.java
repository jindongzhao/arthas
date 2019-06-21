package com.taobao.arthas.manage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.taobao.arthas.manage.bo.TaskBo;
import com.taobao.arthas.manage.enums.TaskStatusEnum;

public class BizTaskManager {
	private static Map<String , List<TaskBo>> taskMap = new HashMap<>();
	
	public static void addTask(TaskBo taskBo) {
		List<TaskBo> taskBoList = taskMap.get(taskBo.getTaskId());
		if(taskBoList == null) {
			taskBoList = new ArrayList<>();
			taskMap.put(taskBo.getTaskId(), taskBoList);
		}
		
		taskBoList.add(taskBo);
	}
	
	/**
	 * 根据AppId查询需要执行的命令列表
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 20 Jun 2019 15:46:25
	 */
	public static List<TaskBo> getTaskListByAppId(String appId) {
		List<TaskBo> boList = new ArrayList<>();
		for(Entry<String, List<TaskBo>> entry : taskMap.entrySet()) {
			for(TaskBo bo : entry.getValue()) {
				if(TaskStatusEnum.INIT.getCode().equals(bo.getTaskStatusCode()) && appId.equals(bo.getAppId())) {
					boList.add(bo);
				}
			}
		}
		return boList;
	}
	
	public static void updateTaskStatus(String taskId, TaskStatusEnum statusEnum) {
		List<TaskBo> taskBoList = taskMap.get(taskId);
		if(taskBoList != null) {
			for(TaskBo taskBo : taskBoList) {
				taskBo.setTaskStatusCode(statusEnum.getCode());
			}
		}
	}
}
