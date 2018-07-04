package com.along101.logclient.event;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.alibaba.fastjson.JSON;
import com.along101.logclient.bean.LogMessage;
import com.along101.logclient.logback.LogMessageBuilder;
import org.junit.Test;

/**
 * Created by yinzuolong on 2017/5/25.
 */
public class LogEventTest {

    LoggerContext loggerContext = new LoggerContext();
    Logger logger = loggerContext.getLogger(LogEventTest.class);


    ConsoleAppender createAppender() {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(loggerContext);
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%date [%thread] %-5level %logger{36}- %msg%n");
        encoder.setContext(loggerContext);
        encoder.start();
        appender.setEncoder(encoder);
        appender.start();
        return appender;
    }


    /**
     * 测试event
     *
     * @throws Exception
     */
    @Test
    public void testEvent() throws Exception {
        LoggingEvent loggingEvent = EventHelper.makeExcetionEvent(logger, Level.INFO, "this is a test message.");
        ConsoleAppender<ILoggingEvent> appender = createAppender();
        appender.doAppend(loggingEvent);
    }

    @Test
    public void testLogEventVo() throws Exception {
        LoggingEvent loggingEvent = EventHelper.makeExcetionEvent(logger, Level.INFO, "this is a test message.");
        LogMessage vo = LogMessageBuilder.build(loggingEvent);
        vo.setLayoutMessage(EventHelper.getLayout(loggerContext).doLayout(loggingEvent));

        String json = JSON.toJSONString(vo);
        System.out.println(json);
    }


    @Test
    public void testLogEventCallerData() throws Exception {
        LoggingEvent loggingEvent = EventHelper.makeExcetionEvent(logger, Level.INFO, "this is a test message.");
        StackTraceElement[] stack = loggingEvent.getCallerData();
        System.out.println(stack);
    }

}
