package com.fr.design.fun;

import com.fr.stable.fun.mark.Immutable;
import com.fr.third.apache.log4j.spi.LoggingEvent;

public interface DesignerLoggingProcessor extends Immutable {
    int CURRENT_LEVEL = 1;

    String XML_TAG = "DesignerLoggingProcessor";

    /**
     * 输出日志对象
     * @param loggingEvent 日志对象
     */
    void printLoggingEvent(LoggingEvent loggingEvent);
}