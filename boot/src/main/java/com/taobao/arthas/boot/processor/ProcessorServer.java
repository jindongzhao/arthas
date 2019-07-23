package com.taobao.arthas.boot.processor;

import com.taobao.arthas.boot.GlobalConfig;
import com.taobao.arthas.boot.processor.telnet.TelnetProcessorServer;

/**
 * 处理管理端发送过来的请求
 * @author zhaojindong
 *
 */
public class ProcessorServer {
	/**
	 * 启动请求处理服务
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 23 Jul 2019 19:56:48
	 */
	public static void start() {
		TelnetProcessorServer telnetProcessorServer = new TelnetProcessorServer();
		telnetProcessorServer.open(GlobalConfig.CONN_TELNET_PORT);
	}
}
