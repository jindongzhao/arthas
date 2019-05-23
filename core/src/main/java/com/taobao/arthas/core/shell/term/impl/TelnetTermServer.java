package com.taobao.arthas.core.shell.term.impl;

import com.taobao.arthas.core.shell.future.Future;
import com.taobao.arthas.core.shell.handlers.Handler;
import com.taobao.arthas.core.shell.term.Term;
import com.taobao.arthas.core.shell.term.TermServer;
import com.taobao.arthas.core.util.LogUtil;
import com.taobao.middleware.logger.Logger;
import io.termd.core.function.Consumer;
import io.termd.core.telnet.netty.NettyTelnetTtyBootstrap;
import io.termd.core.tty.TtyConnection;

import java.util.concurrent.TimeUnit;

/**
 * Encapsulate the Telnet server setup.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TelnetTermServer extends TermServer {

    private static Logger logger = LogUtil.getArthasLogger();

    private NettyTelnetTtyBootstrap bootstrap;
    private String hostIp;
    private int port;
    private long connectionTimeout;
    
    //处理远程客户端的连接请求
    private Handler<Term> termHandler;

    public TelnetTermServer(String hostIp, int port, long connectionTimeout) {
        this.hostIp = hostIp;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
    }

    /*
     * zjd 日志：handler: TermServerTermHandler
     */
    @Override
    public TermServer termHandler(Handler<Term> handler) {
        termHandler = handler;
        return this;
    }
    
    /*
     * zjd listenHandler 为： TermServerListenHandler(...,BindHandler,...)
     */
    @Override
    public TermServer listen(Handler<Future<TermServer>> listenHandler) {
        // TODO: charset and inputrc from options
        bootstrap = new NettyTelnetTtyBootstrap().setHost(hostIp).setPort(port);
        try {
        	//zjd 启动Netty的telnet server
            bootstrap.start(new Consumer<TtyConnection>() {
            	/*
            	 * zjd 应该是相当于 NIO的accept事件，当有client连接到这个server时，会触发
            	 */
                @Override
                public void accept(final TtyConnection conn) {
                	//zjd Helper.loadKeymap():加载命令行快捷键和操作系统命令行处理函数Function的name的映射关系
                	//用TermImpl封装termd建立通信的api
                    termHandler.handle(new TermImpl(Helper.loadKeymap(), conn));
                }
            }).get(connectionTimeout, TimeUnit.MILLISECONDS);
            listenHandler.handle(Future.<TermServer>succeededFuture());
        } catch (Throwable t) {
            logger.error(null, "Error listening to port " + port, t);
            listenHandler.handle(Future.<TermServer>failedFuture(t));
        }
        return this;
    }

    @Override
    public void close() {
        close(null);
    }

    @Override
    public void close(Handler<Future<Void>> completionHandler) {
        if (bootstrap != null) {
            bootstrap.stop();
            if (completionHandler != null) {
                completionHandler.handle(Future.<Void>succeededFuture());
            }
        } else {
            if (completionHandler != null) {
                completionHandler.handle(Future.<Void>failedFuture("telnet term server not started"));
            }
        }
    }

    public int actualPort() {
        return bootstrap.getPort();
    }
}
