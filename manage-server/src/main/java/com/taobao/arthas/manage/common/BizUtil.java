package com.taobao.arthas.manage.common;

import org.springframework.util.DigestUtils;

import com.taobao.arthas.manage.dao.domain.AppConnectionDo;

public class BizUtil {
	public static String getAppId(AppConnectionDo appConnectionDo) {
		String originKey = appConnectionDo.getAppIp() + appConnectionDo.getAppStartCmd();
		return DigestUtils.md5DigestAsHex(originKey.getBytes());
	}
}
