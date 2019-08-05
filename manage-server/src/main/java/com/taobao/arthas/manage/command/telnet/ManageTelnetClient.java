package com.taobao.arthas.manage.command.telnet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 与目标机保持
 * @author zhaojindong
 *
 */
public class ManageTelnetClient {
	/**
	 * 向telnet server发送信息
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 23 Jul 2019 22:02:48
	 */
	public static void sendCommand(String targetTelenetIp, int targetTelnetPort, String command) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ClientInitializer());
			Channel ch = b.connect(targetTelenetIp, targetTelnetPort).sync().channel();
			ch.writeAndFlush(command + "\r\n");
			ch.closeFuture().sync();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
