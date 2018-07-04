package com.along101.logclient.log4j2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.Test;

/**
 * Created by yinzuolong on 2017/6/15.
 */
public class ConsoleAppenderTest {

    private static final Logger LOG = LogManager.getLogger(ConsoleAppenderTest.class);

    @Test
    public void test() throws Exception {

        final String config = "target/test-classes/log4j2-console.xml";
        final LoggerContext ctx = Configurator.initialize(ConsoleAppenderTest.class.getName(), config);
        LOG.fatal("Fatal message.");
        LOG.error("Error message.");
        LOG.warn("Warning message.");
        LOG.info("Information message.");
        LOG.debug("Debug message.");
        LOG.trace("Trace message.");
        try {
            throw new NullPointerException();
        } catch (final Exception e) {
            LOG.error("Error message.", e);
            LOG.catching(Level.ERROR, e);
        }
        LOG.warn("this is ok \n And all \n this have only\t\tblack colour \n and here is colour again?");
        LOG.info("Information message.");
        ctx.stop();
    }
}
