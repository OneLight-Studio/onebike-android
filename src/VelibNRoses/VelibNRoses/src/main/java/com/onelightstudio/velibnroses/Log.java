package com.onelightstudio.velibnroses;

public class Log {

    public static final int NONE = 0;
    public static final int WTF = 1;
    public static final int ERROR = 2;
    public static final int WARN = 3;
    public static final int INFO = 4;
    public static final int DEBUG = 5;
    public static final int VERBOSE = 6;
    public static final int ALL = 7;

    private static final String TAG = "OneBike#";

    private static int level = INFO;

    public static void setLevel(int level) {
        Log.level = level;
    }

    public static void d(String msg) {
        if (level >= DEBUG) {
            android.util.Log.d(TAG + getCallerName(), msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (level >= DEBUG) {
            android.util.Log.d(TAG + getCallerName(), msg, tr);
        }
    }

    public static void e(String msg) {
        if (level >= ERROR) {
            android.util.Log.e(TAG + getCallerName(), msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (level >= ERROR) {
            android.util.Log.e(TAG + getCallerName(), msg, tr);
        }
    }

    public static void i(String msg) {
        if (level >= INFO) {
            android.util.Log.i(TAG + getCallerName(), msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (level >= INFO) {
            android.util.Log.i(TAG + getCallerName(), msg, tr);
        }
    }

    public static void v(String msg) {
        if (level >= VERBOSE) {
            android.util.Log.v(TAG + getCallerName(), msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (level >= VERBOSE) {
            android.util.Log.v(TAG + getCallerName(), msg, tr);
        }
    }

    public static void w(String msg) {
        if (level >= WARN) {
            android.util.Log.w(TAG + getCallerName(), msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (level >= WARN) {
            android.util.Log.w(TAG + getCallerName(), msg, tr);
        }
    }

    public static void w(Throwable tr) {
        if (level >= WARN) {
            android.util.Log.w(TAG + getCallerName(), tr);
        }
    }

    public static void wtf(String msg) {
        if (level >= WTF) {
            android.util.Log.wtf(TAG + getCallerName(), msg);
        }
    }

    public static void wtf(String msg, Throwable tr) {
        if (level >= WTF) {
            android.util.Log.wtf(TAG + getCallerName(), msg, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (level >= WTF) {
            android.util.Log.wtf(TAG + getCallerName(), tr);
        }
    }

    private static String getCallerName() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        return stackTraceElement.getClassName().replace(Log.class.getPackage().getName(), "") + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
    }
}
