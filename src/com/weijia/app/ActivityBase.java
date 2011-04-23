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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import com.weijia.util.LogUtils;

import java.lang.ref.WeakReference;

abstract public class ActivityBase extends Activity {

    /* package */ static final int DIALOG_ID_ACTIVITY_INIT_TIMEOUT = 10000;

    private boolean mCreated;
    private boolean mResumed;
    private boolean mIsActivityInForeground;
    private AsyncActivityInitTask mInitTask;
    private int mDefaultOrientation;

    private static class AsyncActivityInitTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<ActivityBase> mActivityRef;
        private Bundle mState;

        public AsyncActivityInitTask(ActivityBase activity, Bundle state) {
            mActivityRef = new WeakReference<ActivityBase>(activity);
            mState = state;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ActivityBase activity = mActivityRef.get();
            if (activity != null) {
                return ((AppBase) activity.getApplication()).waitInitComplete();
            } else {
                LogUtils.logD("AsyncActivityInitTask: activity is null, cancel!");
                cancel(false);
                return false;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            LogUtils.logD("AsyncActivityInitTask.onCancelled.");
        }

        @Override
        protected void onPostExecute(Boolean success) {
            LogUtils.logD("AsyncActivityInitTask.onPostExecute: " + success);
            super.onPostExecute(success);

            ActivityBase activity = mActivityRef.get();
            if (null == activity) {
                LogUtils.logD("AsyncActivityInitTask: activity is null, finish!");
                return;
            }

            if (success) {
                activity.performCustomCreate(mState);
            } else {
                activity.showDialog(DIALOG_ID_ACTIVITY_INIT_TIMEOUT);
            }
        }
    }

    abstract protected void onCustomCreate(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usingWindowFeature();
        disableOrientationChange();
        setContentView(R.layout.wait_init);

        mInitTask = new AsyncActivityInitTask(this, savedInstanceState);
        mInitTask.execute();
    }

    private void performCustomCreate(Bundle savedInstanceState) {
        LogUtils.logD("ActivityBase.performCustomCreate.");
        onCustomCreate(savedInstanceState);
        mCreated = true;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mIsActivityInForeground && !mResumed) {
                    performCustomResume();
                }
            }
        });
    }

    private void disableOrientationChange() {
        mDefaultOrientation = getRequestedOrientation();
        LogUtils.logD("Default orientation: " + mDefaultOrientation);

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            default:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                break;
        }
    }

    protected void usingWindowFeature() {
    }

    abstract protected void onCustomResume();

    @Override
    protected void onResume() {
        super.onResume();
        performCustomResume();
    }

    private void performCustomResume() {
        LogUtils.logD("ActivityBase.performCustomResume.");
        if (mCreated) {
            onCustomResume();
            mResumed = true;
        }
        mIsActivityInForeground = true;

        // Restore default orientation, this may enable
        // orientation change again.
        setRequestedOrientation(mDefaultOrientation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsActivityInForeground = false;
    }

    @Override
    public void finish() {
        if (!mCreated) {
            LogUtils.logD("Cancelling async init task...");
            mInitTask.cancel(true);
        }
        super.finish();
    }

    @Override
    protected Dialog onCreateDialog(final int id, Bundle args) {
        switch (id) {
            case DIALOG_ID_ACTIVITY_INIT_TIMEOUT:
                return new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_title_error)
                        .setMessage(R.string.msg_app_init_timeout)
                        .setPositiveButton(
                            R.string.button_label_ok,
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, final int which) {
                                    finish();
                                }
                            })
                        .create();
            default:
                return super.onCreateDialog(id);
        }
    }
}
