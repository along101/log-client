package com.along101.logclient.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.status.Status;
import com.along101.logclient.event.EventHelper;
import com.along101.logclient.metrics.ConsoleMetricReporter;
import com.along101.logclient.metrics.MetricContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yinzuolong on 2017/5/25.
 */
public class KafkaAppenderTest {

    private ConsoleMetricReporter reporter = new ConsoleMetricReporter(MetricContext.metricService);
    private LoggerContext loggerContext;

    @Before
    public void before() {
        loggerContext = new LoggerContext();
    }

    @After
    public void after() {
        loggerContext.stop();
    }

    /**
     * XML方式测试
     *
     * @throws Exception
     */
    @Test
    public void testXml() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("kafka.xml");
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        configurator.doConfigure(in);
        Logger logger = loggerContext.getLogger(KafkaAppenderTest.class);
        logger.info("this is a {} message", "test", EventHelper.createException());
        logger.debug("this is a {} message", "test", EventHelper.createException());
        logger = loggerContext.getLogger("com.along1011");
        logger.debug("this is a {} message", "test", EventHelper.createException());

        loggerContext.stop();
        List<Status> list = loggerContext.getStatusManager().getCopyOfStatusList();
        for (Status st : list) {
            System.out.printf("%10s %s %n", st.getLevel(), st.getMessage());
        }
    }

    /**
     * 测试性能
     *
     * @throws Exception
     */
    @Test
    public void testXmlProfile() throws Exception {
        int count = 10000;
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("kafka.xml");
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        configurator.doConfigure(in);
        Logger logger = loggerContext.getLogger(KafkaAppenderTest.class);
        logger.info("this is a {} message", "test", EventHelper.createException());
        Exception ex = EventHelper.createException();
        for (int i = 0; i < count; i++) {
            logger.info("this is a {} message", "test", ex);
        }
        System.out.println("send completed");
        loggerContext.stop();
        reporter.report();
    }

    /**
     * 运行测试后，在kairosdb中查logclient的metric
     *
     * @throws Exception
     */
    @Test
    public void testAppendMetric() throws Exception {
        int count = 1000;
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("kafka.xml");
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        configurator.doConfigure(in);
        Logger logger = loggerContext.getLogger(KafkaAppenderTest.class);
        logger.info("this is a {} message", "test", EventHelper.createException());
        Exception ex = EventHelper.createException();
        for (int i = 0; i < count; i++) {
            logger.info("this is a {} message", "test", ex);
            Thread.sleep(100);
        }
        System.out.println("send completed");
        loggerContext.stop();
    }

    /**
     * 启动后修改配置信息，便于与spring框架集成
     *
     * @throws Exception
     */
    @Test
    public void testRebuild() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("kafka-manual.xml");
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        configurator.doConfigure(in);

        for (Iterator<KafkaAppender> it = KafkaAppender.getKafkaAppenders().iterator(); it.hasNext(); ) {
            KafkaAppender appender = it.next();
            if (!appender.isStarted()) {
                appender.setAppId("456789");
                appender.doStart();
            }
        }
        Logger logger = loggerContext.getLogger(KafkaAppenderTest.class);

        logger.info("this is a {} message", "test", EventHelper.createException());
        logger.debug("this is a {} message", "test", EventHelper.createException());

        logger = loggerContext.getLogger("com.along1011");
        logger.debug("this is a {} message", "test", EventHelper.createException());

        loggerContext.stop();
        List<Status> list = loggerContext.getStatusManager().getCopyOfStatusList();
        for (Status st : list) {
            System.out.printf("%10s %s %n", st.getLevel(), st.getMessage());
        }
    }
}
