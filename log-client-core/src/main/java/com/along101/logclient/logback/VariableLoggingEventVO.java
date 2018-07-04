package com.along101.logclient.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import ch.qos.logback.classic.spi.ThrowableProxyVO;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

import java.util.Map;

/**
 * 可以设置属性的Event
 * Created by yinzuolong on 2017/5/27.
 */
public class VariableLoggingEventVO implements ILoggingEvent {

    private static final int NULL_ARGUMENT_ARRAY = -1;
    private static final String NULL_ARGUMENT_ARRAY_ELEMENT = "NULL_ARGUMENT_ARRAY_ELEMENT";

    private String threadName;
    private String loggerName;
    private LoggerContextVO loggerContextVO;

    private transient Level level;
    private String message;

    private transient String formattedMessage;

    private transient Object[] argumentArray;

    private ThrowableProxyVO throwableProxy;
    private StackTraceElement[] callerDataArray;
    private Marker marker;
    private Map<String, String> mdcPropertyMap;
    private long timeStamp;

    public static VariableLoggingEventVO build(ILoggingEvent le) {
        VariableLoggingEventVO ledo = new VariableLoggingEventVO();
        ledo.loggerName = le.getLoggerName();
        ledo.loggerContextVO = le.getLoggerContextVO();
        ledo.threadName = le.getThreadName();
        ledo.level = (le.getLevel());
        ledo.message = (le.getMessage());
        ledo.argumentArray = (le.getArgumentArray());
        ledo.marker = le.getMarker();
        ledo.mdcPropertyMap = le.getMDCPropertyMap();
        ledo.timeStamp = le.getTimeStamp();
        ledo.throwableProxy = ThrowableProxyVO.build(le.getThrowableProxy());
        // add caller data only if it is there already
        // fixes http://jira.qos.ch/browse/LBCLASSIC-145
        ledo.callerDataArray = le.getCallerData();
        return ledo;
    }

    public String getThreadName() {
        return threadName;
    }

    public LoggerContextVO getLoggerContextVO() {
        return loggerContextVO;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage() {
        if (formattedMessage != null) {
            return formattedMessage;
        }

        if (argumentArray != null) {
            formattedMessage = MessageFormatter.arrayFormat(message, argumentArray).getMessage();
        } else {
            formattedMessage = message;
        }

        return formattedMessage;
    }

    public Object[] getArgumentArray() {
        return argumentArray;
    }

    public IThrowableProxy getThrowableProxy() {
        return throwableProxy;
    }

    public StackTraceElement[] getCallerData() {
        return callerDataArray;
    }

    public boolean hasCallerData() {
        return callerDataArray != null;
    }

    public Marker getMarker() {
        return marker;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public long getContextBirthTime() {
        return loggerContextVO.getBirthTime();
    }

    public LoggerContextVO getContextLoggerRemoteView() {
        return loggerContextVO;
    }

    public Map<String, String> getMDCPropertyMap() {
        return mdcPropertyMap;
    }

    public Map<String, String> getMdc() {
        return mdcPropertyMap;
    }

    public void prepareForDeferredProcessing() {
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public void setLoggerContextVO(LoggerContextVO loggerContextVO) {
        this.loggerContextVO = loggerContextVO;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFormattedMessage(String formattedMessage) {
        this.formattedMessage = formattedMessage;
    }

    public void setArgumentArray(Object[] argumentArray) {
        this.argumentArray = argumentArray;
    }

    public void setThrowableProxy(ThrowableProxyVO throwableProxy) {
        this.throwableProxy = throwableProxy;
    }

    public void setCallerDataArray(StackTraceElement[] callerDataArray) {
        this.callerDataArray = callerDataArray;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setMdcPropertyMap(Map<String, String> mdcPropertyMap) {
        this.mdcPropertyMap = mdcPropertyMap;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
