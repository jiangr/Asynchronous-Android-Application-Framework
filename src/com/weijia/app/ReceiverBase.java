/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.weijia.app;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weijia.util.LogUtils;

import java.util.concurrent.LinkedBlockingQueue;

abstract public class ReceiverBase extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        LogUtils.logD("ReceiverBase.onReceive.");
        InnerService.enqueue(new Runnable() {
            @Override
            public void run() {
                onCustomReceive(context, intent);
            }
        });
        context.startService(new Intent(context, InnerService.class));
    }

    abstract protected void onCustomReceive(Context context, Intent intent);

    public static class InnerService extends IntentService {

        private static final LinkedBlockingQueue<Runnable> QUEUE =
                new LinkedBlockingQueue<Runnable>();

        public static void enqueue(Runnable r) {
            QUEUE.offer(r);
        }

        public InnerService() {
            super("Async Receiver Executor");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            LogUtils.logD("InnerService.onHandleIntent.");

            if (((AppBase) getApplication()).waitInitComplete()) {
                for (Runnable r = QUEUE.poll(); r != null; r = QUEUE.poll()) {
                    r.run();
                }
            } else {
                LogUtils.logD("InnerService: timeout.");
            }
        }
    }
}
