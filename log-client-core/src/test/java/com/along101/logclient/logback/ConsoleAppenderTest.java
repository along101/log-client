package com.along101.logclient.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yinzuolong on 2017/5/23.
 */
public class ConsoleAppenderTest {

    @Test
    public void name() throws Exception {
        Logger logger = LoggerFactory.getLogger(ConsoleAppenderTest.class);
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        configurator.doConfigure(this.getClass().getClassLoader().getResourceAsStream("console.xml"));
        logger.info("1");
        logger = LoggerFactory.getLogger("com.along1011");
        logger.info("2");
        logger = LoggerFactory.getLogger("com.along1011.test");
        logger.info("3");

        logger.error("{}", "123", new Exception("tt"));

    }
}
