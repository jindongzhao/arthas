package com.taobao.arthas.core.shell.handlers.term;

import com.taobao.arthas.core.shell.handlers.Handler;
import com.taobao.arthas.core.shell.term.impl.TermImpl;
import io.termd.core.function.Consumer;

/**
 * 实现termd的Consumer接口，当有客户端命令输入时，这里的accept接口会被调用
 * @author beiwei30 on 23/11/2016.
 */
public class RequestHandler implements Consumer<String> {
    private TermImpl term;
    private final Handler<String> lineHandler;	//实际处理命令行的类：ShellLineHandler

    /**
     * zjd lineHandler: ShellLineHandler
     * @param term
     * @param lineHandler
     */
    public RequestHandler(TermImpl term, Handler<String> lineHandler) {
        this.term = term;
        this.lineHandler = lineHandler;
    }

    @Override
    public void accept(String line) {
        term.setInReadline(false);
        lineHandler.handle(line);
    }
}
