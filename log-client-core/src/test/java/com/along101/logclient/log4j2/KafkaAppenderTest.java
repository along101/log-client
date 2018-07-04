package com.along101.logclient.log4j2;

import com.along101.logclient.event.EventHelper;
import com.along101.logclient.metrics.ConsoleMetricReporter;
import com.along101.logclient.metrics.MetricContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.Test;

/**
 * Created by yinzuolong on 2017/6/15.
 */
public class KafkaAppenderTest {
    private static final Logger LOG = LogManager.getLogger(ConsoleAppenderTest.class);
    private ConsoleMetricReporter reporter = new ConsoleMetricReporter(MetricContext.metricService);
    @Test
    public void test() throws Exception {
        final String config = "target/test-classes/log4j2-kafka.xml";
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
        }
        ctx.stop();
    }

    @Test
    public void testProfile() throws Exception {
        int count = 10000;
        final String config = "target/test-classes/log4j2-kafka.xml";
        final LoggerContext ctx = Configurator.initialize(ConsoleAppenderTest.class.getName(), config);
        Exception ex = EventHelper.createException();
        for (int i = 0; i < count; i++) {
            LOG.info("this is a {} message", "test", ex);
        }
        ctx.stop();
        reporter.report();
    }
}
