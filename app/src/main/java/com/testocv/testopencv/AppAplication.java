package com.testocv.testopencv;

import android.app.Application;

/**
 * Created by zhangshexin on 2018/11/1.
 */

public class AppAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        System.load("libopencv_java3.so");
    }
}
