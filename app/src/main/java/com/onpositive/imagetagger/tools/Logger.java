package com.onpositive.imagetagger.tools;

import android.util.Log;

import com.onpositive.imagetagger.BuildConfig;

public class Logger {
    private final String logTag;
    private boolean isLogging = false;

    public Logger(Class c) {
        logTag = c.getSimpleName();
        isLogging = BuildConfig.IS_LOGS_ENABLE;
    }

    public void log(String message) {
        if (isLogging)
            Log.d(logTag, message);
    }

    public void error(String message) {
        if (isLogging)
            Log.e(logTag, message);
    }

    public void info(String message) {
        if (isLogging)
            Log.i(logTag, message);
    }
}
