package com.testocv.testopencv;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.testocv.testopencv.databinding.ActivityOpencvWatermarkBinding;
import com.testocv.testopencv.util.DFTUtil;
import com.testocv.testopencv.util.ImgWatermarkUtil;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成盲水印和提取水印
 */
public class Activity_opencv_watermark extends AppCompatActivity implements View.OnClickListener {
    private Bitmap originalBitmap1;
    private ActivityOpencvWatermarkBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        originalBitmap1 = BitmapUtil.readBitmap(this, R.drawable.comput_me1);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_opencv_watermark);
        binding.imageView.setImageBitmap(originalBitmap1);
        initView();
    }

    private void initView() {
        binding.buttonGenerat.setOnClickListener(this);
        binding.buttonGetmark.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_generat:
                //生成水印图
                try {
                    String mark = binding.editTextMarkcontent.getText().toString();
                    generatMark(mark);
                    Toast.makeText(this, "生成成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "生成失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_getmark:
                //提出水印
                try {
                    if (tranMat != null) {
//                        getMark();
                        getWM();
                        Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Mat tranMat;

    /**
     * 可用的生成水印
     */
    private void generatMark(String mark) {
        Mat originalMat = new Mat();
        Utils.bitmapToMat(originalBitmap1, originalMat);

        Scalar scalar = new Scalar(0, 0, 255);
        Point point = new Point(20, 40);
        tranMat = transformImageWithText(originalMat, mark, point, 1D, scalar);


//        tranMat=ImgWatermarkUtil.addImageWatermarkWithText(originalMat,mark);

        Utils.matToBitmap(tranMat, originalBitmap1);
        binding.imageView.setImageBitmap(originalBitmap1);
    }

    private void generatMark2(String mark) {
//        DFTUtil.getInstance().transformImageWithText();
    }

    /**
     * 提取水印
     */
    private void getMark() {
//        Mat mat=ImgWatermarkUtil.getImageWatermarkWithText(tranMat);

        Mat invDft = new Mat();
        Utils.bitmapToMat(originalBitmap1, invDft);
        invDft = DFTUtil.getInstance().transformImage(invDft);
//        Core.dft(tranMat,invDft);
//        Mat restoredImage = new Mat();
//        invDft.convertTo(restoredImage, CvType.CV_8U);
        Utils.matToBitmap(invDft, originalBitmap1);
        binding.imageView.setImageBitmap(originalBitmap1);
    }

    private String TAG = this.getClass().getName();

    /**
     * 可用的提取水印方法
     */
    private void getWM() {
        Mat mat = new Mat();
        Utils.bitmapToMat(originalBitmap1, mat);


        int p = mat.rows() * mat.cols();
        Log.e(TAG, "改变前的大小: " + p);

        List<Mat> planes = new ArrayList<>();
        List<Mat> allPlanes = new ArrayList<>();
        Mat complexImage = new Mat();
        // optimize the dimension of the loaded image
        Mat padded = splitSrc(mat, allPlanes);
        padded.convertTo(padded, CvType.CV_32F);
        // prepare the image planes to obtain the complex image
        planes.add(padded);
        planes.add(Mat.zeros(padded.size(), CvType.CV_32F));
        // prepare a complex image for performing the dft
        Core.merge(planes, complexImage);
        // dft
        Core.dft(complexImage, complexImage);
        Core.split(complexImage, planes);
        int after = complexImage.rows() * complexImage.cols();
        Log.e(TAG, "改变后的大小: " + after);
        Mat mat1 = new Mat();
        Mat mat2 = new Mat();
        planes.get(0).convertTo(mat1, CvType.CV_8UC4);
        planes.get(1).convertTo(mat2, CvType.CV_8UC4);

        complexImage.convertTo(complexImage, CvType.CV_8UC4);
        Bitmap newB = Bitmap.createBitmap(complexImage.width(), complexImage.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat1, newB);
        binding.imageView.setImageBitmap(newB);
    }

    private Mat transformImageWithText(Mat image, String watermarkText, Point point, Double fontSize, Scalar scalar) {
        List<Mat> planes = new ArrayList<>();
        List<Mat> allPlanes = new ArrayList<>();
        Mat complexImage = new Mat();
        // optimize the dimension of the loaded image
        Mat padded = splitSrc(image, allPlanes);
        padded.convertTo(padded, CvType.CV_32F);
        // prepare the image planes to obtain the complex image
        planes.add(padded);
        planes.add(Mat.zeros(padded.size(), CvType.CV_32F));
        // prepare a complex image for performing the dft
        Core.merge(planes, complexImage);
        // dft
        Core.dft(complexImage, complexImage);
        // 频谱图上添加文本

        Imgproc.putText(complexImage, watermarkText, point, Core.FONT_HERSHEY_DUPLEX, fontSize, scalar, 3, 8, false);
        Core.flip(complexImage, complexImage, -1);
        Imgproc.putText(complexImage, watermarkText, point, Core.FONT_HERSHEY_DUPLEX, fontSize, scalar, 3, 8, false);
        Core.flip(complexImage, complexImage, -1);
        return antitransformImage(complexImage, allPlanes);
    }


    public Mat antitransformImage(Mat complexImg, List<Mat> allPlanes) {
        Mat invDft = new Mat();
        Core.idft(complexImg, invDft, Core.DFT_SCALE | Core.DFT_REAL_OUTPUT, 0);
        Mat restoredImg = new Mat();
        invDft.convertTo(restoredImg, CvType.CV_8U);
        if (allPlanes.size() == 0) {
            allPlanes.add(restoredImg);
        } else {
            allPlanes.set(0, restoredImg);
        }
        Mat lastImg = new Mat();
        Core.merge(allPlanes, lastImg);
        return lastImg;
    }

    private Mat splitSrc(Mat img, List<Mat> plans) {
        if (plans == null) {
            plans = new ArrayList<>();
        }
        Core.split(img, plans);
        Mat padded;
        if (plans.size() > 0) {
            padded = plans.get(0);
        } else
            padded = img;
        return padded;
    }

}
