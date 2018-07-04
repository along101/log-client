package com.along101.logclient.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.along101.logclient.bean.LogMessage;
import com.along101.logclient.trace.CatTraceChain;
import com.along101.logclient.utils.IPUtil;

import java.util.HashMap;

/**
 * Created by yinzuolong on 2017/6/5.
 */
public class LogMessageBuilder {

    public static LogMessage build(ILoggingEvent le) {
        LogMessage vo = new LogMessage();
        vo.setLogName(le.getLoggerName());
        vo.setThreadName(le.getThreadName());
        vo.setLevel(le.getLevel().levelStr);
        vo.setMessage(le.getFormattedMessage());
        vo.setMdc(le.getMDCPropertyMap());
        vo.setTimeStamp(le.getTimeStamp());
        vo.getTags().put("HOST_IP", IPUtil.getLocalIP());
        String catTrace = CatTraceChain.getTrace();
        if (!"".equalsIgnoreCase(catTrace) && null != catTrace)
            vo.getTags().put("catTrace", catTrace);
        return vo;
    }

    public static LogMessage buildTest() {
        LogMessage vo = new LogMessage();
        vo.setLogName(LogMessageBuilder.class.getName());
        vo.setThreadName(Thread.currentThread().getName());
        vo.setLevel("DEBUG");
        vo.setMessage("test message");
        vo.setMdc(new HashMap<>());
        vo.setTimeStamp(System.currentTimeMillis());
        vo.getTags().put("HOST_IP", IPUtil.getLocalIP());
        return vo;
    }
}
