package com.fr.design.mainframe.loghandler;

import com.fr.log.FineLoggerFactory;
import com.fr.third.apache.log4j.Level;
import com.fr.third.apache.log4j.spi.LoggingEvent;
import com.fr.third.apache.log4j.spi.ThrowableInformation;

/**
 * 设计器日志记录
 */
public class DesignerLogger {
    public static final int INFO_INT = Level.INFO.toInt();

    public static final int ERROR_INT = Level.ERROR.toInt();

    public static final int WARN_INT = Level.WARN.toInt();

    /**
     * 记录LoggingEvent对象
     * @param event
     */
    public static void log(LoggingEvent event) {
        if (event == null) {
            return;
        }
        int intLevel = event.getLevel().toInt();
        ThrowableInformation information = event.getThrowableInformation();
        if (intLevel == INFO_INT) {
            FineLoggerFactory.getLogger().info(event.getRenderedMessage());
        } else if (intLevel == ERROR_INT) {
            FineLoggerFactory.getLogger().error(event.getRenderedMessage(), information == null ? null : information.getThrowable());
        } else if (intLevel == WARN_INT) {
            FineLoggerFactory.getLogger().warn(event.getRenderedMessage(), information == null ? null : information.getThrowable());
        }
    }
}