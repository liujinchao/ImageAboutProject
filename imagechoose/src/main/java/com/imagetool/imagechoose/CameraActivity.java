package com.imagetool.imagechoose;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * 类名称：CameraActivity2
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/8 09:54
 * 描述：TODO
 * 最近修改时间：2016/12/8 09:54
 * 修改人：Modify by liujc
 */
public class CameraActivity extends Activity{
    private Camera camera;
    private SurfaceView cameraScuface;
    private Camera.Parameters parameters;
    private final String CAMEAR_PATH = "/DCIM/MyPic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser_activity_camera);
        initView();
    }


    protected void initView() {
        cameraScuface = (SurfaceView) findViewById(R.id.camera_surface);
        View cameraShutter = findViewById(R.id.camera_shutter);
        cameraShutter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera != null) {
                    /**
                     * takePicture()方法需要传入三个监听参数 第一个监听器；当用户按下快门时激发该监听器
                     * 第二个监听器；当相机获取原始照片时激发该监听器 第三个监听器；当相机获取JPG照片时激发该监听器
                     * */
                    camera.takePicture(null, null, new PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            if (data != null && data.length != 0) {
                                File file = new File(Environment.getExternalStorageDirectory() + CAMEAR_PATH);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                try {
                                    File file1 = new File(file, Calendar.getInstance().getTimeInMillis() + ".jpg");
                                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file1));
                                    bufferedOutputStream.write(data);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            /**
                             * 停止输出预览画面，并重新开启预览画面
                             * */
                            camera.stopPreview();
                            camera.startPreview();
                        }
                    });
                }
            }
        });
        /**
         * 获取SurfaceHolder对象，并设置屏幕常亮
         * */
        cameraScuface.getHolder().setKeepScreenOn(true);
        /**
         * 添加SurfaceView的显示源
         * */
        cameraScuface.getHolder().addCallback(new SurfaceCallBack());
    }
    /**
     * SurafaceView 的显示源
     * */
    private class SurfaceCallBack implements Callback {
        /**
         * SurfaceView 状态发生改变时
         * */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            /**
             * 自动对焦
             * */
            camera.autoFocus(new AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        initCamera();
                    }
                }
            });
        }
        /**
         * SurfaceView 创建时执行
         * */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                /**
                 * SurfaceView 被创建时尝试开启摄像头
                 * */
                camera = Camera.open();
                /**
                 * 设置摄像头的预览画面显示到SurfaceView上
                 * */
                camera.setPreviewDisplay(holder);
                /**
                 * 开启预览
                 * */
                camera.startPreview();
            } catch (Exception e) {
                Log.e("asksky", e.getMessage());
            }
        }
        /**
         * SurfaceView 被销毁时执行
         * */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                /**
                 * 当SurfaceView 被销毁时，释放Camera资源
                 * */
                camera.release();
                camera = null;
            }
        }
    }
    private void initCamera() {
        /**
         * 获取相机参数
         * */
        parameters = camera.getParameters();
        /**
         * 设置相机格式
         * */
        parameters.setPictureFormat(PixelFormat.JPEG);
        /**
         * 设置预览照片时每秒显示多少帧的最小值和最大值 parameters.setPreviewFpsRange(4, 10);
         */
        /**
         * 设置闪光的的模式
         * */
        parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
        /**
         * 设置相机对焦模式，为连续对焦
         * */
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        /**
         * 设置照片质量，100为最大值
         * */
        parameters.setJpegQuality(100);
        /**
         * 预览画面的宽高
         * */
        int PreviewWidth = 0;
        int PreviewHeight = 0;
        /**
         * 获取相机支持的所有预览画面的尺寸大小
         * */
        List<Size> size_list = parameters.getSupportedPreviewSizes();
        if (size_list.size() > 0) {
            Iterator<Size> itor = size_list.iterator();
            while (itor.hasNext()) {
                Camera.Size cur = itor.next();
                /**
                 * 循环遍历，选择最大尺寸预览画面
                 * 注意：此处选择预览画面是可以先获取屏幕尺寸比例，
                 * 然后依次比较所有可用预览画面比例，选取与屏幕相同比例，或比例相差极小的预览画面
                 * 这样就不需要进行下面的比例换算了
                 * */
                if (cur.width >= PreviewWidth && cur.height >= PreviewHeight) {
                    PreviewWidth = cur.width;
                    PreviewHeight = cur.height;
                }
            }
        }
        /**
         * 设置预览画面大小尺寸
         * */
        parameters.setPreviewSize(PreviewWidth, PreviewHeight);
        /**
         * 预览画面大小设置完成后，若直接投射到SerfaceView,此时， 若是SurfaceView的宽高比与相机返回的预览画面的宽高比不同
         * 就会出现预览画面被拉伸的问题，因此，此处需要根据预览画面大小来调节SurfaceView的大小
         * */
        /**
         * 获取屏幕大小
         * */
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        /**
         * 判断预览画面与屏幕大小，若屏幕的高度大于预览画面的高度，计算二者之间的比例
         * 若屏幕的高度小于预览画面的高度，则比率为1,下面的代码可精简为： double rate_h =
         * h_screen>PreviewHeight?(double)h_screen/(double)PreviewHeight:1;
         * */
        double rate_h = 1;
        if (h_screen > PreviewHeight) {
            rate_h = (double) h_screen / (double) PreviewHeight;
        }
        /**
         * 判断预览画面与屏幕大小，若屏幕的宽度大于预览画面的宽度，计算二者之间的比例 具体同上
         * */
        double rate_w = 1;
        if (w_screen > PreviewWidth) {
            rate_h = (double) w_screen / (double) PreviewWidth;
        }
        /**
         * 判断高度比与宽度比，选择最大比率，以此来保证SerfaceView的宽高大于等于屏幕大小， 同时宽高比等于预览画面的宽高比
         * 预览画面的宽高乘以计算出的宽高比就是此时SerfaceView应该显示的宽高
         * */
        double rate = rate_h > rate_w ? rate_h : rate_w;
        int w = (int) (rate * PreviewWidth);
        int h = (int) (rate * PreviewHeight);
        /**
         * 修改SerfaceView的宽高，此时再将预览画面映射到SerfaceView,就不会出现屏幕拉伸的问题了
         * 外层容器是FrameLayout 则用android.widget.FrameLayout.LayoutParams
         * */
        android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, h);
        cameraScuface.setLayoutParams(layoutParams);
        /**
         * 设置照片大小尺寸 首先获取所有支持的尺寸大小,for循环获取最大的尺寸大小
         * */
        List<Size> picSizeValues = camera.getParameters().getSupportedPictureSizes();
        int picw = 0;
        int pich = 0;
        for (Size size : picSizeValues) {
            if (size.width > picw && size.height > pich) {
                picw = size.width;
                pich = size.height;
            }
        }
        parameters.setPictureSize(picw, pich);
        /**
         * 将参数设置到相机中
         * */
        camera.setParameters(parameters);
        /**
         * 开启预览
         * */
        camera.startPreview();
        /**
         * 根据屏幕设置旋转角度
         * */
        camera.setDisplayOrientation(getPreviewDegree(CameraActivity.this));
        /**
         * 移除自动对焦 （这一句必须加上）
         * */
        camera.cancelAutoFocus();
    }
    /**
     * 该方法将根据当前屏幕的旋转的角度返回相应的旋转角度值
     * */
    private int getPreviewDegree(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }
}
