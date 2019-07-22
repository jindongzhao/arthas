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
     * Arthas 内部日志Logger
     */
    private static final Logger arthasLogger;

    public static final String LOGGER_FILE = LoggerHelper.getLogFile("arthas", "arthas.log");

    static {
        LogLog.setQuietMode(true);

        LoggerHelper.setPattern("arthas-cache", "%d{yyyy-MM-dd HH:mm:ss.SSS}%n%m%n");

        arthasLogger = LoggerFactory.getLogger("arthas");
        arthasLogger.activateAppenderWithTimeAndSizeRolling("arthas", "arthas.log", "UTF-8", "100MB");
        arthasLogger.setLevel(Level.DEBUG);
        arthasLogger.setAdditivity(false);
    }

    public static Logger getArthasLogger() {
        return arthasLogger;
    }

}
