package com.imagetool.imagechoose.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 类名称：CameraSurfaceView
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/11/14
 * 描述：TODO
 */
public class CameraSurfaceView  extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    private static final String TAG = "CameraSurfaceView";

    private Context mContext;
    private SurfaceHolder holder;
    private Camera mCamera;
    private String imageName = "";

    private int cameraPosition = 1;//0代表前置摄像头,1代表后置摄像头,默认打开前置摄像头
    private int cameraCount = 0;//获得相机的摄像头数量

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        if (mCamera == null) {
            int  CammeraIndex = findBackCamera();
            mCamera = Camera.open(CammeraIndex);//开启相机
            try {
                mCamera.setPreviewDisplay(holder);//摄像头画面显示在Surface上
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged");
        //设置参数并开始预览
        setCameraParams(mCamera);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        mCamera.stopPreview();//停止预览
        mCamera.release();//释放相机资源
        mCamera = null;
        holder = null;
    }

    @Override
    public void onAutoFocus(boolean success, Camera Camera) {
        if (success) {
            Log.i(TAG, "onAutoFocus success="+success);
        }
    }

    // 拍照瞬间调用
    private Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.i(TAG,"shutter");
        }
    };

    // 获得没有压缩过的图片数据
    private Camera.PictureCallback raw = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            Log.i(TAG, "raw");

        }
    };

    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            BufferedOutputStream bos = null;
            Bitmap bm = null;
            if (data != null && data.length != 0) {
                File file = new File(imageName);
                File baseFile = new File(file.getParent());
                if (!baseFile.exists()){
                    baseFile.mkdirs();
                }
                try {
                    // 获得图片
                    bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                    switch (cameraPosition) {
                        case 0://前
                            bm = rotateBitmapByDegree(bm,270);
                            break;
                        case 1:
                            bm = rotateBitmapByDegree(bm,getPreviewDegree(mContext));
                            break;
                    }

                    if (!file.exists()){
                        file.createNewFile();
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中
                    if (takePhotoComplete != null){
                        takePhotoComplete.takePhotoComplete(imageName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bos.flush();//输出
                        bos.close();//关闭
                        bm.recycle();// 回收bitmap空间
                        mCamera.stopPreview();// 关闭预览
                        mCamera.startPreview();// 开启预览
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public Camera getCamera() {
        return mCamera;
    }

    public void setAutoFocus(){
        mCamera.autoFocus(this);
    }

    public void takePicture(){
        //设置参数,并拍照
        setCameraParams(mCamera);
        // 当调用camera.takePiture方法后，camera关闭了预览，这时需要调用startPreview()来重新开启预览
        mCamera.takePicture(shutter, raw, jpeg);
    }

    private void setCameraParams(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        /**
         * 设置相机格式
         * */
        parameters.setPictureFormat(PixelFormat.JPEG);
        /**
         * 设置闪光的的模式
         * */
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        /**
         * 设置照片质量，100为最大值
         * */
        parameters.setJpegQuality(100);
        /**
         * 设置相机对焦模式，为连续对焦
         * */
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        /**
         * 预览画面的宽高
         * */
        int PreviewWidth = 0;
        int PreviewHeight = 0;
        /**
         * 获取相机支持的所有预览画面的尺寸大小
         * */
        List<Camera.Size> size_list = parameters.getSupportedPreviewSizes();
        if (size_list.size() > 0) {
            Iterator<Camera.Size> itor = size_list.iterator();
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, h);
        this.setLayoutParams(layoutParams);
        /**
         * 设置照片大小尺寸 首先获取所有支持的尺寸大小,for循环获取最大的尺寸大小
         * */
        List<Camera.Size> picSizeValues = camera.getParameters().getSupportedPictureSizes();
        int picw = 0;
        int pich = 0;
        for (Camera.Size size : picSizeValues) {
            if (size.width > picw && size.height > pich) {
                picw = size.width;
                pich = size.height;
            }
        }
        parameters.setPictureSize(picw, pich);

        camera.cancelAutoFocus();//自动对焦。
        mCamera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
        camera.setParameters(parameters);

    }

    /**
     * 改变摄像头
     */
    public void changeCamera() {
        //切换前后摄像头
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                        mCamera.setDisplayOrientation(getPreviewDegree(mContext));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setCameraParams(mCamera);
                    mCamera.startPreview();//开始预览
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                        mCamera.setDisplayOrientation(getPreviewDegree(mContext));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setCameraParams(mCamera);
                    mCamera.startPreview();//开始预览
                    cameraPosition = 1;
                    break;
                }
            }
        }
    }

    /**
     * 查找前置摄像头
     *
     * @return
     */
    private int findFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    /**
     * 查找后置摄像头
     *
     * @return
     */
    private int findBackCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    /**
     * 该方法将根据当前屏幕的旋转的角度返回相应的旋转角度值
     * */
    private int getPreviewDegree(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = WM.getDefaultDisplay().getRotation();
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
    private ITakePhotoComplete takePhotoComplete;

    /** 拍照完成后回调接口 */
    public interface  ITakePhotoComplete{
        void takePhotoComplete(String imgPath);
    }

    public void setITakePhotoComplete(ITakePhotoComplete takePhotoComplete) {
        this.takePhotoComplete = takePhotoComplete;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

}
