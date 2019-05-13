package com.taobao.arthas.core.shell.handlers.server;

import com.taobao.arthas.core.shell.handlers.Handler;
import com.taobao.arthas.core.shell.impl.ShellServerImpl;
import com.taobao.arthas.core.shell.term.Term;

/**
 * @author beiwei30 on 23/11/2016.
 */
public class TermServerTermHandler implements Handler<Term> {
    private ShellServerImpl shellServer;

    public TermServerTermHandler(ShellServerImpl shellServer) {
        this.shellServer = shellServer;
    }

    /*
     * zjd  term： TermImpl
     */
    @Override
    public void handle(Term term) {
    	//又回到shellServerImpl中，处理客户端的建立连接请求
        shellServer.handleTerm(term);
    }
}
