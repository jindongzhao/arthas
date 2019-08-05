package com.taobao.arthas.manage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.manage.common.HttpResponseVo;
import com.taobao.arthas.manage.vo.LoginUserVo;

/**
 * 管理页面请求 controller
 * 
 * @author zhaojindong
 *
 */
@RestController
public class UserController {

	/**
	 * 登录
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 5 Aug 2019 10:29:44
	 */
	@RequestMapping("/manage/login")
	public String login(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "password", required = true) String password) {
		LoginUserVo loginUserVo = new LoginUserVo();
		loginUserVo.setId(1L);
		loginUserVo.setName("admin");

		return JSON.toJSONString(HttpResponseVo.success(loginUserVo));
	}

}
