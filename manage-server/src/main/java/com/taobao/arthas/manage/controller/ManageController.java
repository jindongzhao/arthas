package com.taobao.arthas.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.manage.common.HttpResponseVo;
import com.taobao.arthas.manage.constants.enums.TaskStatusEnum;
import com.taobao.arthas.manage.constants.enums.TaskTypeEnum;
import com.taobao.arthas.manage.dao.AppConnectionDao;
import com.taobao.arthas.manage.dao.OptTaskDao;
import com.taobao.arthas.manage.dao.domain.AppConnectionDo;
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
	private AppConnectionDao appConnectionDao;
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
	@PostMapping("/manage/getAliveAppList")
	public String getAliveAppList() {
		List<AppConnectionDo> resultList = appConnectionDao.findAll();
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
	public String doAttach(@RequestParam(name = "connIdList", required = true) String connIdListStr,
			@RequestParam(name = "loginUserId", required = true) Long loginUserId) {
		String[] idArr = connIdListStr.split(",");

		// 创建总任务
		OptTaskDo parentTaskDo = new OptTaskDo();
		parentTaskDo.setTaskTypeCode(TaskTypeEnum.ATTACH.getCode());
		parentTaskDo.setTaskStatusCode(TaskStatusEnum.INIT.getCode());
		parentTaskDo.setTaskParam(connIdListStr);
		optTaskDao.save(parentTaskDo);

		// 创建子任务
		for (String connId : idArr) {
			OptTaskDo subTaskBo = new OptTaskDo();
			subTaskBo.setTaskTypeCode(TaskTypeEnum.ATTACH.getCode());
			subTaskBo.setTaskStatusCode(TaskStatusEnum.INIT.getCode());
			subTaskBo.setAppConnectionId(Long.valueOf(connId));
			subTaskBo.setParentTaskId(parentTaskDo.getId());
			optTaskDao.save(subTaskBo);
		}

		return JSON.toJSONString(HttpResponseVo.success(null));
	}

}
