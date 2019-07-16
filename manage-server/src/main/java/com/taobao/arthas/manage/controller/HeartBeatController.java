package com.taobao.arthas.manage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.arthas.common.ManageRespsCodeEnum;
import com.taobao.arthas.common.ManageRpcCommandEnum;
import com.taobao.arthas.common.ManageRpcUtil;
import com.taobao.arthas.common.dto.HeartBeatReqDto;
import com.taobao.arthas.common.dto.HeartBeatRespDto;
import com.taobao.arthas.common.dto.ManageTaskDto;
import com.taobao.arthas.manage.constants.enums.TaskStatusEnum;
import com.taobao.arthas.manage.constants.enums.TaskTypeEnum;
import com.taobao.arthas.manage.dao.AppConnectionDao;
import com.taobao.arthas.manage.dao.OptTaskDao;
import com.taobao.arthas.manage.dao.domain.AppConnectionDo;
import com.taobao.arthas.manage.dao.domain.OptTaskDo;

/**
 * 心跳维持 controller
 * 
 * @author zhaojindong
 *
 */
@RestController
public class HeartBeatController {

	@Resource
	private AppConnectionDao appConnectionDao;
	@Resource
	private OptTaskDao optTaskDao;

	/**
	 * 客户端发送请求告诉服务端，自己存活着
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 14 Jun 2019 14:04:43
	 */
	@PostMapping("/heartBeat/isAlive")
	public String isAlive(@RequestParam(name = "params", required = true) String params) {
		HeartBeatReqDto reqDto = ManageRpcUtil.deserializeReqParam(params, HeartBeatReqDto.class);
		
		//保存心跳数据
		AppConnectionDo appDo = saveOrUpdateConnection(reqDto.getAppIp(),reqDto.getPid(),reqDto.getAppStartCmd());
		
		// 返回的心跳数据
		HeartBeatRespDto heartBeatResp = new HeartBeatRespDto();
		heartBeatResp.setResultCode(ManageRespsCodeEnum.SUCCESS.getCode());

		// 查询需要执行的命令
		List<ManageTaskDto> taskDtoList = getExcuteTransactionList(appDo.getId());
		
		heartBeatResp.setTaskDtoList(taskDtoList);

		return ManageRpcUtil.serializeRspsResult(heartBeatResp);
	}

	private AppConnectionDo saveOrUpdateConnection(String appIp, Integer pid, String appStartCmd) {
		// 查询客户端是否存在
		AppConnectionDo appDo = appConnectionDao.getByAppipAndPid(appIp, pid);
		if (appDo == null) {
			// 不存在客户端连接，保存连接信息
			AppConnectionDo appConnectionDo = new AppConnectionDo();
			appConnectionDo.setAppIp(appIp);
			appConnectionDo.setPid(pid);
			appConnectionDo.setAppStartCmd(appStartCmd);
			appConnectionDo.setLastConnectionTime(System.currentTimeMillis());
			appConnectionDao.save(appConnectionDo);
			return appConnectionDo;
		} else {
			// 更新心跳连接时间
			appDo.setLastConnectionTime(System.currentTimeMillis());
			appConnectionDao.updateConnectionTime(appDo.getId(), System.currentTimeMillis());
			return appDo;
		}
	}
	
	/**
	 * 根据app connectionId 查询需要执行的事务
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 15 Jul 2019 22:38:10
	 */
	private List<ManageTaskDto> getExcuteTransactionList(Long connId){
		List<ManageTaskDto> taskList = new ArrayList<>();
		List<OptTaskDo> initTaskList = optTaskDao.getByConnectionIdAndStatus(connId,TaskStatusEnum.INIT.getCode());
		
		if (initTaskList != null) {
			for (OptTaskDo taskDo : initTaskList) {
				// attach命令
				if (TaskTypeEnum.ATTACH.getCode().equals(taskDo.getTaskTypeCode())) {
					ManageTaskDto txDto = new ManageTaskDto();
					txDto.setTaskId(taskDo.getId());
					txDto.setCommand(ManageRpcCommandEnum.COMMAND_ATTACH.getCode());
					taskList.add(txDto);
				}

				// 更新命令状态为执行中
				optTaskDao.updateStatus(taskDo.getId(), TaskStatusEnum.DOING.getCode());
			}
		}
		return taskList;
	}
}
