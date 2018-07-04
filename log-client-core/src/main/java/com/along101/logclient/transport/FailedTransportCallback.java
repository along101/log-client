package com.along101.logclient.transport;

import com.along101.logclient.bean.LogMessage;

public interface FailedTransportCallback {
    void onFailedDelivery(LogMessage evt, Throwable throwable);
}
