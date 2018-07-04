package com.along101.logclient.logback;

import ch.qos.logback.classic.net.SMTPAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.along101.logclient.sensitive.Desensitiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/5/27.
 */
public class MailAppender extends SMTPAppender {

    @Override
    protected void append(ILoggingEvent leo) {
        VariableLoggingEventVO eventVO = VariableLoggingEventVO.build(leo);
        filter(eventVO);
        super.append(eventVO);
    }

    private void filter(VariableLoggingEventVO eventVO) {
        Map<String, String> newMdc = new HashMap<>(eventVO.getMdc());
        eventVO.setMdcPropertyMap(newMdc);
        eventVO.setFormattedMessage(Desensitiver.desensitive(eventVO.getFormattedMessage()));
    }
}
