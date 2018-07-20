package com.fr.design.mainframe.loghandler;


import com.fr.third.apache.log4j.AppenderSkeleton;
import com.fr.third.apache.log4j.Level;
import com.fr.third.apache.log4j.spi.LoggingEvent;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/18 0018.
 */
public class DesignerLogAppender extends AppenderSkeleton {

    public DesignerLogAppender() {
        this.layout = new com.fr.third.apache.log4j.PatternLayout("%d{HH:mm:ss} %t %p [%c] %m%n");
    }

    protected void append(LoggingEvent event) {
        this.subAppend(event);
    }

    public boolean requiresLayout() {
        return true;
    }

    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;

    }

    public void subAppend(LoggingEvent event) {
        synchronized (DesignerLogHandler.getInstance()) {
            Level level = event.getLevel();
            String msg = this.layout.format(event);
            DesignerLogHandler.getInstance().printRemoteLog(msg, level, new Date());
        }
    }
}

