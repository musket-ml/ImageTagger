package com.onpositive.imagetagger.tools;

import android.util.Log;

import com.onpositive.imagetagger.BuildConfig;

public class Logger {
    private final String logTag;
    private boolean isLogging = false;

    public Logger(Class c) {
        logTag = c.getName();
        isLogging = BuildConfig.IS_LOGS_ENABLE;
    }

    public Logger() {
        logTag = getCallerClass();
        isLogging = BuildConfig.IS_LOGS_ENABLE;
    }

    public void log(String message) {
        if (isLogging)
            Log.d(logTag, getCallerName() + " " + getCallPlace() + " : " + message);
    }

    public void error(String message) {
        if (isLogging)
            Log.e(logTag, getCallerName() + " " + getCallPlace() + " : " + message);
    }

    public void info(String message) {
        if (isLogging)
            Log.i(logTag, getCallerName() + " " + getCallPlace() + " : " + message);
    }

    private String getCallerName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[4];
        String loggedMethodName = e.getMethodName();
        return loggedMethodName;
    }

    private String getCallerClass() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[4];
        return e.getClassName();
    }

    private String getCallPlace() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[4];
        String callPlace = "(" + e.getFileName() + ":" + e.getLineNumber() + ")";
        return callPlace;
    }
}
