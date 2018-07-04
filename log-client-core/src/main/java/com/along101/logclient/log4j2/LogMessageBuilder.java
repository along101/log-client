package com.along101.logclient.log4j2;

import com.along101.logclient.bean.LogMessage;
import com.along101.logclient.trace.CatTraceChain;
import com.along101.logclient.utils.IPUtil;
import org.apache.logging.log4j.core.LogEvent;

/**
 * Created by yinzuolong on 2017/6/5.
 */
public class LogMessageBuilder {

    public static LogMessage build(LogEvent le) {
        LogMessage vo = new LogMessage();
        vo.setLogName(le.getLoggerName());
        vo.setThreadName(le.getThreadName());
        vo.setLevel(le.getLevel().name());
        vo.setMessage(le.getMessage().getFormattedMessage());
        vo.setMdc(le.getContextMap());
        vo.setTimeStamp(le.getTimeMillis());
        vo.getTags().put("HOST_IP", IPUtil.getLocalIP());
        String catTrace = CatTraceChain.getTrace();
        if (!"".equalsIgnoreCase(catTrace) && null != catTrace)
            vo.getTags().put("catTrace", catTrace);
        return vo;
    }
}
