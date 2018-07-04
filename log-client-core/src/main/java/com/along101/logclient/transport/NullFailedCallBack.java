package com.along101.logclient.transport;

import com.along101.logclient.bean.LogMessage;

/**
 * Created by yinzuolong on 2017/6/16.
 */
public class NullFailedCallBack implements FailedTransportCallback{
    @Override
    public void onFailedDelivery(LogMessage evt, Throwable throwable) {

    }
}
