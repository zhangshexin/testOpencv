package com.testocv.testopencv;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.testocv.testopencv.databinding.ActivityMainBinding;

/**
 * Created by zhangshexin on 2018/11/2.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.btnCheckDistinct.setOnClickListener(this);
        binding.btnComputPicture.setOnClickListener(this);
        binding.btnFilterPeople.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_checkDistinct:
                //1、检查图片清晰度
                startActivity2(CheckDistinctActivity.class);
                break;
            case R.id.btn_computPicture:
                //3、与给定人脸进行对比

                break;
            case R.id.btn_filterPeople:
                //2、判断图中是否有人，定位人脸位置，并对人脸进行剥离
                break;

        }
    }

    private void startActivity2(Class<?> t){
        startActivity(new Intent(this,t));
    }
}
