package com.testocv.testopencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.features2d.BOWKMeansTrainer;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Bitmap originalBitmap;
    private Bitmap currentBitmap;
    private TextView score;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        originalBitmap = BitmapUtil.readBitmap(this, R.drawable.people);
        currentBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight());
        imgView = findViewById(R.id.img);
        findViewById(R.id.btn_check).setOnClickListener(this);
        score = findViewById(R.id.recode);
        imgView.setImageBitmap(originalBitmap);
    }

    /**
     * Sobel算子
     */
    private void Sobel() {

        Mat originalMat = new Mat();
        Utils.bitmapToMat(originalBitmap, originalMat);

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
        //计算两个方向上梯度绝对值
        Core.convertScaleAbs(grad_x, abs_grad_x);
        Core.convertScaleAbs(grad_y, abs_grad_y);
        //计算结果梯度
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 1, sobel);
        //将Mat转换为位图
        Utils.matToBitmap(sobel, currentBitmap);
        Scalar c = Core.mean(sobel);
        double dd = c.val[0];
        score.setText("清晰度为:" + dd);
//        loadImageToImageView();
    }

    /**
     * 设置图像
     */
    private void loadImageToImageView() {
        ImageView imgView = findViewById(R.id.img);
        imgView.setImageBitmap(originalBitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check:
                // 检查图片清晰度
                Sobel();
                break;
        }
    }
}
