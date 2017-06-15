package com.wenld.app_multitypeadapter.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntRange;

import java.util.concurrent.Executor;

/**
 * <p/>
 * Author: 温利东 on 2017/6/15 14:53.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class MainThreadExecutor implements Executor {
    long delayMillis = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public MainThreadExecutor() {
    }

    public MainThreadExecutor(@IntRange(from = 0) long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public void execute(Runnable r) {
        handler.postDelayed(r, delayMillis);
    }
}