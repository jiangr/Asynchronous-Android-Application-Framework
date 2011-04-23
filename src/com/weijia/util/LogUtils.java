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

package com.weijia.util;

import android.util.Log;

public class LogUtils {
    private static final String LOG_TAG = "AsyncAppInit";

    public static void logD(String msg) {
        if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(LOG_TAG, String.format("%d) %s", Thread.currentThread().getId(), msg));
        }
    }

    public static void logV(String msg) {
        if (Log.isLoggable(LOG_TAG, Log.VERBOSE)) {
            Log.v(LOG_TAG, String.format("%d) %s", Thread.currentThread().getId(), msg));
        }
    }

    public static void logI(String msg) {
        if (Log.isLoggable(LOG_TAG, Log.INFO)) {
            Log.i(LOG_TAG, String.format("%s", msg));
        }
    }

    public static void logW(String msg) {
        if (Log.isLoggable(LOG_TAG, Log.WARN)) {
            Log.w(LOG_TAG, String.format("%s", msg));
        }
    }

    public static void logE(String msg) {
        if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
            Log.e(LOG_TAG, String.format("%s", msg));
        }
    }

    public static void logE(String msg, Throwable e) {
        if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
            Log.e(LOG_TAG, String.format("%s", msg), e);
        }
    }
}
