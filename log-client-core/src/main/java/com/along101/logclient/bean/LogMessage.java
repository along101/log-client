package com.along101.logclient.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/5/25.
 */
public class LogMessage implements Serializable {

    private String appId;
    private String threadName;
    private String logName;
    private String level;
    private String message;
    private String layoutMessage;
    private long timeStamp;
    private Map<String, String> mdc;
    private Map<String, String> tags = new HashMap<>();


    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLayoutMessage() {
        return layoutMessage;
    }

    public void setLayoutMessage(String layoutMessage) {
        this.layoutMessage = layoutMessage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Map<String, String> getMdc() {
        return mdc;
    }

    public void setMdc(Map<String, String> mdc) {
        this.mdc = mdc;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
