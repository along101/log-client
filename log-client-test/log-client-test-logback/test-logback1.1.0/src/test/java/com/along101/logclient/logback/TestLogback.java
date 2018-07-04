package com.along101.logclient.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.Status;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by yinzuolong on 2017/5/26.
 */
public class TestLogback {

    private static Logger logger = LoggerFactory.getLogger(TestLogback.class);

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 10; i++) {
            logger.error("this is a {} message {}", "error", i, new Exception());
        }
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();
        List<Status> list = lc.getStatusManager().getCopyOfStatusList();
        for (Status st : list) {
            System.out.printf("%2s %s %n", st.getLevel(), st.getMessage());
            if(st.getLevel() >= Status.ERROR){
                st.getThrowable().printStackTrace();
            }
            Assert.assertTrue(st.getLevel() < Status.ERROR);
        }
    }
}
