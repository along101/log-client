package com.along101.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluatorBase;
import com.along101.logclient.utils.IPUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gaochuanjun
 * @since 2017/1/5
 */
public class SMTPFrequencyEvaluator extends EventEvaluatorBase<ILoggingEvent> {

    //默认超过1000条重发
    private static final long DEFAULT_MAX_COUNT = 1000;

    //默认5分钟重新发送
    private static final long DEFAULT_EXPIRED_TIME = 5 * 60 * 1000;

    private static final ConcurrentHashMap<String, Message> evaluateMap = new ConcurrentHashMap<>();
    private long maxCount = DEFAULT_MAX_COUNT;
    private long expiredTime = DEFAULT_EXPIRED_TIME;

    @Override
    public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
        setEventMdc(event,"ip", IPUtil.getLocalIP());
        StackTraceElement stackTraceElement = event.getCallerData()[0];
        String key = stackTraceElement.toString();
        if (evaluateMap.containsKey(key)) {
            Message message = evaluateMap.get(key);
            if (message.isTrigger()) {
                setEventMdc(event, "error_count", String.valueOf(message.count));
                message.reset();
                return true;
            } else {
                return false;
            }
        } else {
            setEventMdc(event, "error_count", "1");
            evaluateMap.put(key, new Message());
            return true;
        }
    }

    public long getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(long maxCount) {
        this.maxCount = maxCount;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime * 1000;
    }

    /**
     * 如果没有设置mdc，event.getMDCPropertyMap()为空，无法设置
     *
     * @param event
     * @param key
     * @param value
     */
    private void setEventMdc(ILoggingEvent event, String key, String value) {
        try {
            event.getMDCPropertyMap().put(key, value);
        } catch (Exception e) {
        }
    }

    class Message {

        private final AtomicLong count;
        private long start;

        Message() {
            this(1);
        }

        Message(long count) {
            this(count, System.currentTimeMillis());
        }

        Message(long count, long start) {
            this.count = new AtomicLong(count);
            this.start = start;
        }

        //AppendBase的doAppend方法是同步的，没有并发问题
        boolean isTrigger() {
            long value = count.addAndGet(1);
            if (value >= maxCount || (System.currentTimeMillis() - start) >= expiredTime) {
                return true;
            }
            return false;
        }

        void reset() {
            start = System.currentTimeMillis();
            count.set(0);
        }
    }

}
