package com.taobao.arthas.common;

import com.alibaba.fastjson.JSON;

public class ManageRpcUtil {
	/**
	 * 向manage server发送请求
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 15 Jul 2019 20:05:52
	 */
	public static <R,T> R sendManageRequest(String url, T requestObj, Class<R> responseClass){
		System.out.println("请求manage server："+url+"--------"+JSON.toJSONString(requestObj));
		String responseStr = HttpUtils.doPost(url, HttpUtils.generateRequestParam(requestObj));
		System.out.println("manage server返回："+JSON.toJSONString(responseStr));
		R respObj = JSON.parseObject(responseStr, responseClass);
		return respObj;
	}
	
	/**
	 * 反序列化请求参数
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 15 Jul 2019 20:55:46
	 */
	public static <T> T deserializeReqParam(String param, Class<T> requestClass) {
		return JSON.parseObject(param, requestClass);
	}
	
	/**
	 * 序列化请求响应结果
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 15 Jul 2019 22:54:05
	 */
	public static <R> String serializeRspsResult(R responseObj) {
		return JSON.toJSONString(responseObj);
	}
}
