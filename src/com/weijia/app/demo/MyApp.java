package com.weijia.app.demo;

import com.weijia.app.AppBase;
import com.weijia.util.LogUtils;

public class MyApp extends AppBase {
    @Override
    protected void onCustomCreateAsync() {
        try {
            LogUtils.logD("MyAsyncInitApplication is sleeping...");
            Thread.sleep(10000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
