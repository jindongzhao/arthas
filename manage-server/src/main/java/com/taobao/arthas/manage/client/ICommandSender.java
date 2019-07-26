package com.taobao.arthas.manage.client;

/**
 * 命令发送类接口
 * @author zhaojindong
 *
 */
public interface ICommandSender {
	
	String sendCommand(String cmd);
}
