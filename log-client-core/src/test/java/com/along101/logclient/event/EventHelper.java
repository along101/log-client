package com.along101.logclient.event;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.along101.logclient.bean.LogMessage;
import com.along101.logclient.logback.LogMessageBuilder;

/**
 * Created by yinzuolong on 2017/5/25.
 */
public class EventHelper {

    public static LoggingEvent makeLoggingEvent(Logger logger, Level level, String message, Exception ex) {
        return new LoggingEvent(ch.qos.logback.core.pattern.FormattingConverter.class.getName(), logger, level, message, ex, null);
    }

    public static Exception createException() {
        Exception ex = null;
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            ex = e;
        }
        return ex;
    }

    public static LoggingEvent makeExcetionEvent(Logger logger, Level level, String message) {
        return makeLoggingEvent(logger, level, message, createException());
    }

    static PatternLayout layout;

    public static synchronized PatternLayout getLayout(LoggerContext lc) {
        if (layout == null) {
            layout = new PatternLayout();
            layout.setPattern("%date [%thread] %-5level %logger{36}- %msg%n");
        }
        layout.setContext(lc);
        layout.start();
        return layout;
    }

    public static LogMessage createLogEventVo(LoggerContext lc, LoggingEvent event) {
        LogMessage vo = LogMessageBuilder.build(event);
        PatternLayout layout = getLayout(lc);
        vo.setLayoutMessage(layout.doLayout(event));
        vo.setAppId("789456");
        return vo;
    }

}
