package com.along101.logclient.trace;

import com.dianping.cat.Cat;
import com.dianping.cat.message.spi.MessageTree;

/**
 * Created by yinzuolong on 2017/5/26.
 */
public class CatTraceChain {

    static private boolean loadCatClass = false;

    static {
        try {
            Class.forName("com.dianping.cat.Cat");
            loadCatClass = true;
        } catch (Exception e) {

        }
    }

    public static String getTrace() {
        if (loadCatClass) {
            return getCatTrace();
        }
        return null;
    }

    static private String getCatTrace() {
        MessageTree msgTree = Cat.getManager().getThreadLocalMessageTree();
        if (msgTree != null) {
            String msgId = msgTree.getMessageId();
            if (msgId == null) {
                msgId = Cat.getCurrentMessageId();
            }
            return String.format("http://cat.along101corp.com/cat/r/m/%s", msgId);
        }
        return null;
    }
}
