package com.taobao.arthas.common.log;

import com.taobao.middleware.logger.Level;
import com.taobao.middleware.logger.Logger;
import com.taobao.middleware.logger.LoggerFactory;
import com.taobao.middleware.logger.support.LogLog;
import com.taobao.middleware.logger.support.LoggerHelper;

/**
 * Arthas日志
 * Created by vlinux on 15/3/8.
 */
public class ArthasLogUtil {
    /**
     * Arthas client Logger
     */
    private static final Logger arthasClientLogger;
    /**
     * Arthas manage server Logger
     */
    private static final Logger arthasManageLogger;

    static {
        LogLog.setQuietMode(true);

        LoggerHelper.setPattern("arthas-cache", "%d{yyyy-MM-dd HH:mm:ss.SSS}%n%m%n");
        
        arthasClientLogger = LoggerFactory.getLogger("arthas_client");
        arthasClientLogger.activateAppenderWithTimeAndSizeRolling("arthas", "arthas_client.log", "UTF-8", "100MB");
        arthasClientLogger.setLevel(Level.DEBUG);
        arthasClientLogger.setAdditivity(false);
        
        arthasManageLogger = LoggerFactory.getLogger("arthas_manage");
        arthasManageLogger.activateAppenderWithTimeAndSizeRolling("arthas", "arthas_manage.log", "UTF-8", "100MB");
        arthasManageLogger.setLevel(Level.DEBUG);
        arthasManageLogger.setAdditivity(false);
    }

    public static Logger getArthasClientLogger() {
        return arthasClientLogger;
    }
    
    public static Logger getArthasManageLogger() {
        return arthasManageLogger;
    }

}
