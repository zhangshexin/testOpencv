package com.testocv.testopencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by zhangshexin on 2018/11/1.
 */

public class BitmapUtil {

    public static Bitmap readBitmap(Context context, int id) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;//表示16位位图 565代表对应三原色占的位数
        opt.inInputShareable = true;
        opt.inPurgeable = true;//设置图片可以被回收
        InputStream is = context.getResources().openRawResource(id);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
