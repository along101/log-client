package com.along101.logclient.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * Created by yinzuolong on 2017/5/26.
 */
public class NullAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent eventObject) {

    }
}
