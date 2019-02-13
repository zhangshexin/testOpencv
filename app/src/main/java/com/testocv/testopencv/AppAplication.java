package com.testocv.testopencv;

import android.app.Application;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;


/**
 * Created by zhangshexin on 2018/11/1.
 */

public class AppAplication extends Application {
    private LoaderCallbackInterface loaderCallbackInterface;
    @Override
    public void onCreate() {
        super.onCreate();
//        System.load("libopencv_java3.so");
        loaderCallbackInterface=new LoaderCallbackInterface() {
            @Override
            public void onManagerConnected(int status) {

            }

            @Override
            public void onPackageInstall(int operation, InstallCallbackInterface callback) {

            }
        };
        if(!OpenCVLoader.initDebug()){
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,loaderCallbackInterface);
        }else{
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}
