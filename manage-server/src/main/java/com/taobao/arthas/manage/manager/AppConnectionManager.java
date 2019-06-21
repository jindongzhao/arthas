package com.taobao.arthas.manage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.DigestUtils;

import com.taobao.arthas.manage.bo.AppConnectionInfoBo;

public class AppConnectionManager {
	private static Map<String, AppConnectionInfoBo> connectionMap = new HashMap<>();
	
	public static void addOrUpdate(AppConnectionInfoBo appInfo) {
		String id = getAppId(appInfo);
		appInfo.setId(id);
		if(connectionMap.get(id) == null) {
			appInfo.setLastConnectionTime(System.currentTimeMillis());
			connectionMap.put(id, appInfo);
		}else {
			appInfo.setLastConnectionTime(System.currentTimeMillis());
			connectionMap.put(id, appInfo);
		}
	}
	
	public static List<AppConnectionInfoBo> getAliveAppList() {
		List<AppConnectionInfoBo> resultList = new ArrayList<>();
		Set<Entry<String, AppConnectionInfoBo>> entrySet = connectionMap.entrySet();
		if(entrySet != null) {
			for(Entry<String, AppConnectionInfoBo> entry : entrySet) {
				resultList.add(entry.getValue());
			}
		}
		return resultList;
	}
	
	public static String getAppId(AppConnectionInfoBo appInfo) {
		String originKey = appInfo.getAppIp()+appInfo.getAppStartCmd();
		return DigestUtils.md5DigestAsHex(originKey.getBytes());
	}
}
