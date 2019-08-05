package com.taobao.arthas.manage.service;

import org.springframework.stereotype.Component;

import com.taobao.arthas.common.ManageRpcCommandEnum;
import com.taobao.arthas.manage.command.telnet.ManageTelnetClient;

/**
 * app连接客户端service
 * @author zhaojindong
 *
 */
@Component
public class AppClientService {
	/**
	 * 执行attach
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 5 Aug 2019 15:44:32
	 */
	public void asynchDoAttach(final String targetTelnetIp, final Integer targetTelnetPort) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ManageTelnetClient.sendCommand(targetTelnetIp, targetTelnetPort, ManageRpcCommandEnum.COMMAND_ATTACH.getCode());
			}
		}
		).start();
	}
}
