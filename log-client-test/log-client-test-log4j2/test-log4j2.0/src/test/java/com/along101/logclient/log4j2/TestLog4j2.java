package com.along101.logclient.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by yinzuolong on 2017/5/26.
 */
public class TestLog4j2 {

    private static Logger logger = LoggerFactory.getLogger(TestLog4j2.class);

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 10; i++) {
            logger.error("this is a {} message {}", "error", i, new Exception());
        }
        LoggerContext context = (LoggerContext) LogManager.getContext();
        context.stop();
    }

}
