package com.weijia.app.demo;

import android.content.Context;
import android.content.Intent;

import com.weijia.app.ReceiverBase;
import com.weijia.util.LogUtils;

public class MyReceiver extends ReceiverBase {

    @Override
    protected void onCustomReceive(Context context, Intent intent) {
        LogUtils.logD("MyReceiver.onCustomReceive.");
    }
}
