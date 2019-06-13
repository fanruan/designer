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
     *
     * @param event
     */
    public static void log(LoggingEvent event) {
        if (event == null) {
            return;
        }
        LogParser.parse(event).log(event);
    }

    public enum LogParser {
        DEFAULT(-1) {
            @Override
            public void log(LoggingEvent event) {

            }
        },
        INFO(Level.INFO.toInt()) {
            @Override
            public void log(LoggingEvent event) {
                FineLoggerFactory.getLogger().info(event.getRenderedMessage());
            }
        },
        WARN(Level.WARN.toInt()) {
            @Override
            public void log(LoggingEvent event) {
                ThrowableInformation information = event.getThrowableInformation();
                FineLoggerFactory.getLogger().warn(event.getRenderedMessage(), information == null ? null : information.getThrowable());
            }
        },

        ERROR(Level.ERROR.toInt()) {
            @Override
            public void log(LoggingEvent event) {
                ThrowableInformation information = event.getThrowableInformation();
                FineLoggerFactory.getLogger().error(event.getRenderedMessage(), information == null ? null : information.getThrowable());
            }
        };
        private int level;

        LogParser(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public static LogParser parse(LoggingEvent event) {
            int intLevel = event.getLevel().toInt();
            for (LogParser logParser : values()) {
                if (logParser.getLevel() == intLevel) {
                    return logParser;
                }
            }
            return DEFAULT;

        }

        public void log(LoggingEvent event) {
        }
    }
}