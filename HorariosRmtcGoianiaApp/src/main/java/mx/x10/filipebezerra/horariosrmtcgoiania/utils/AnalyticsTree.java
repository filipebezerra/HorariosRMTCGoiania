/*
 * Copyright 2014 Uwe Trottmann
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

package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.content.Context;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import timber.log.Timber;

/**
 * A customized {@link timber.log.Timber.DebugTree} that logs to Crashlytics and Google Analytics.
 * Always drops debug and verbose logs.
 * @author Uwe Trottmann, Filipe Bezerra
 * @version 2.3, 09/01/2016
 */
public class AnalyticsTree extends Timber.DebugTree {
    private final Context context;

    public AnalyticsTree(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.ERROR) {
            // remove any stack trace attached by Timber
            if (message != null) {
                int newLine = message.indexOf('\n');
                if (newLine > 0) {
                    message = message.substring(0, newLine);
                }
            }
        }

        // drop empty messages
        if (message == null) {
            return;
        }
        // drop debug and verbose logs
        if (priority == Log.DEBUG || priority == Log.VERBOSE) {
            return;
        }

        // transform priority into string
        String level = null;
        switch (priority) {
            case Log.INFO:
                level = "INFO";
                break;
            case Log.WARN:
                level = "WARN";
                break;
            case Log.ERROR:
                level = "ERROR";
                break;
        }

        // finally log to crashlytics
        Crashlytics.log(level + "/" + tag + ": " + message);
    }
}