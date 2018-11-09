package com.testocv.testopencv;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.testocv.testopencv.databinding.ComputPictureActivityBinding;

/**
 * Created by zhangshexin on 2018/11/2.
 */

public class ComputPictureActivity extends AppCompatActivity {
    private ComputPictureActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.comput_picture_activity);
    }
}
