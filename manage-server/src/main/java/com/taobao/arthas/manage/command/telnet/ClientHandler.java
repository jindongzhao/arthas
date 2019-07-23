package com.taobao.arthas.manage.command.telnet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler  extends SimpleChannelInboundHandler<String> {
	//打印读取到的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.err.println("telent client收到server返回信息："+msg);
    }
	//异常数据捕获
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
