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

package com.weijia.app.demo;

import android.content.Intent;
import android.os.Bundle;

import com.weijia.app.ActivityBase;
import com.weijia.app.R;
import com.weijia.util.LogUtils;

public class MyActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendBroadcast(new Intent("com.weijia.app.demo.intent.asyncinit"));
        LogUtils.logD("MyActivity.onCreate.");
    }

    @Override
    protected void onCustomCreate(Bundle savedInstanceState) {
        LogUtils.logD("MyActivity.onCustomCreate.");
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.logD("MyActivity.onStart.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.logD("MyActivity.onResume.");
    }

    @Override
    protected void onCustomResume() {
        LogUtils.logD("MyActivity.onCustomResume.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.logD("MyActivity.onPause.");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.logD("MyActivity.onSaveInstanceState.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.logD("MyActivity.onStop.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.logD("MyActivity.onDestroy.");
    }
}