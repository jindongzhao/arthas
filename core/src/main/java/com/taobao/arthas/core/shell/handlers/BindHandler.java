package com.taobao.arthas.core.shell.handlers;

import com.taobao.arthas.core.shell.future.Future;
import com.taobao.arthas.core.util.LogUtil;
import com.taobao.middleware.logger.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ralf0131 2017-04-24 18:23.
 */
public class BindHandler implements Handler<Future<Void>> {

    private static final Logger logger = LogUtil.getArthasLogger();

    private AtomicBoolean isBindRef;

    public BindHandler(AtomicBoolean isBindRef) {
        this.isBindRef = isBindRef;
    }
    
    /**
     * 启动netty server失败等情况发生时，会调用这个方法，打印error日志，并设置isBind为false。
     */
    @Override
    public void handle(Future<Void> event) {
        if (event.failed()) {
            logger.error(null, "Error listening term server:", event.cause());
            isBindRef.compareAndSet(true, false);
        }
    }
}
