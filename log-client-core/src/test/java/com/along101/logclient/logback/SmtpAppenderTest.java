package com.along101.logclient.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yinzuolong on 2017/5/26.
 */
public class SmtpAppenderTest {

    @Test
    public void testMail() throws Exception {
        Logger logger = LoggerFactory.getLogger(ConsoleAppenderTest.class);
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        configurator.doConfigure(this.getClass().getClassLoader().getResourceAsStream("smtp.xml"));
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            logger.error("test error  6221560501640810  123213", e);
        }

        Thread.sleep(5000);
    }
}
