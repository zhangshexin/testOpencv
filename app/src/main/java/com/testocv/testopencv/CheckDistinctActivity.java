package com.testocv.testopencv;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.testocv.testopencv.databinding.ActivityCheckDistinctBinding;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CheckDistinctActivity extends AppCompatActivity implements View.OnClickListener {
    private Bitmap originalBitmap1;
    private Bitmap originalBitmap2;
    private ActivityCheckDistinctBinding binding;

    private String TAG=getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_distinct);
        originalBitmap1 = BitmapUtil.readBitmap(this, R.drawable.people);
        originalBitmap2 = BitmapUtil.readBitmap(this, R.drawable.people_filter);
        binding.btnCheck1.setOnClickListener(this);
        binding.btnCheck2.setOnClickListener(this);
        binding.img.setImageBitmap(originalBitmap1);
        binding.image2.setImageBitmap(originalBitmap2);
    }

    /**
     * Sobel算子
     */
    private void Sobel(int witch) {
        Log.e(TAG, "开始时间: "+System.currentTimeMillis());
        Mat originalMat = new Mat();
        if (witch == 1)
            Utils.bitmapToMat(originalBitmap1, originalMat);
        else
            Utils.bitmapToMat(originalBitmap2, originalMat);


        Mat grayMat = new Mat();
        //用来保存结果的Mat
        Mat sobel = new Mat();
        //分别用于保存梯度和绝对梯度的Mat
        Mat grad_x = new Mat();
        Mat abs_grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_y = new Mat();

        //将图像转换为灰度
        Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        //计算水平方向梯度
        Imgproc.Sobel(grayMat, grad_x, CvType.CV_16S, 1, 0, 3, 1, 0);
        //计算垂直方向梯度
        Imgproc.Sobel(grayMat, grad_y, CvType.CV_16S, 0, 1, 3, 1, 0);

        Imgproc.Sobel(grayMat,sobel,CvType.CV_16U,1,1);
        //计算两个方向上梯度绝对值
        Core.convertScaleAbs(grad_x, abs_grad_x);
        Core.convertScaleAbs(grad_y, abs_grad_y);
        //计算结果梯度
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 1, sobel);

        Scalar c = Core.mean(sobel);
        double dd = c.val[0];
        if (witch == 1)
            binding.t1.setText("清晰度为:" + dd);
        else
            binding.t2.setText("清晰度为:" + dd);

        Log.e(TAG, "结束时间: "+System.currentTimeMillis());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check1:
                // 检查图片清晰度
                Sobel(1);
                break;
            case R.id.btn_check2:
                // 检查图片清晰度
                Sobel(2);
                break;
        }
    }
}
