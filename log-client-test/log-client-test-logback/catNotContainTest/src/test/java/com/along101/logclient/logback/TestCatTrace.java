package com.along101.logclient.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.along101.logclient.bean.LogMessage;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by yinzuolong on 2017/5/26.
 */
public class TestCatTrace {


    private LoggerContext loggerContext = new LoggerContext();

    @Test
    public void testTrace() throws Exception {
        Logger logger = loggerContext.getLogger(TestCatTrace.class);
        LoggingEvent event = makeLoggingEvent(logger, Level.ERROR, "this is an error message.", null);
        LogMessage eventVo = LogMessageBuilder.build(event);
        Map<String, String> tags = eventVo.getTags();
        System.out.println(tags);
        Assert.assertNull(tags.get("catTrace"));
    }

    public LoggingEvent makeLoggingEvent(Logger logger, Level level, String message, Exception ex) {
        return new LoggingEvent(ch.qos.logback.core.pattern.FormattingConverter.class.getName(), logger, level, message, ex, null);
    }
}
