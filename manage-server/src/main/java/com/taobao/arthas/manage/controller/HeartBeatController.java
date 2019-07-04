package com.taobao.arthas.manage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.common.dto.HeartBeatRespDto;
import com.taobao.arthas.manage.common.BizUtil;
import com.taobao.arthas.manage.constants.enums.ResponseResultEnum;
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
		AppConnectionDo appConnectionDo = JSON.parseObject(params, AppConnectionDo.class);
		appConnectionDao.save(appConnectionDo);

		HeartBeatRespDto heartBeatResp = new HeartBeatRespDto();
		heartBeatResp.setCode(ResponseResultEnum.SUCCESS.getCode());

		// 查询需要执行的命令
		List<String> commandList = new ArrayList<>();
		String appId = BizUtil.getAppId(appConnectionDo);
		List<OptTaskDo> taskDoList = optTaskDao.getByAppId(appId);

		if (taskDoList != null) {
			for (OptTaskDo taskDo : taskDoList) {
				// 初始化状态的task
				if (TaskStatusEnum.INIT.getCode().equals(taskDo.getTaskStatusCode())) {
					// attach命令
					if (TaskTypeEnum.ATTACH.getCode().equals(taskDo.getTaskTypeCode())) {
						commandList.add("attach");
					}

					// 更新命令状态为执行中
					optTaskDao.updateStatus(taskDo.getId(), TaskStatusEnum.DOING.getCode());
				}
			}
		}

		heartBeatResp.setCommandList(commandList);

		return JSON.toJSONString(heartBeatResp);
	}

}
