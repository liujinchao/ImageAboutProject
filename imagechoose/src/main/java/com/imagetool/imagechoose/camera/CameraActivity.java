package com.imagetool.imagechoose.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagetool.imagechoose.R;

/**
 * 类名称：CameraActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/22 14:55
 * 描述：自定义相机
 */
public class CameraActivity extends Activity implements View.OnClickListener,RectOnCamera.IAutoFocus, CameraSurfaceView.ITakePhotoComplete {
    private CameraSurfaceView mCameraSurfaceView;
    private RectOnCamera mRectOnCamera;
    private ImageView takePicBtn;
    private TextView take_photo_back,camera_change;
    public static final String IMAGE_PATH = "image_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser_activity_camera);
        initView();
    }

    private void initView() {
        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
        mCameraSurfaceView.setITakePhotoComplete(this);
        mRectOnCamera = (RectOnCamera) findViewById(R.id.rectOnCamera);
        takePicBtn= (ImageView) findViewById(R.id.camera_take_photo);
        take_photo_back= (TextView) findViewById(R.id.take_photo_back);
        camera_change= (TextView) findViewById(R.id.camera_change);
        mRectOnCamera.setIAutoFocus(this);
        takePicBtn.setOnClickListener(this);
        take_photo_back.setOnClickListener(this);
        camera_change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.camera_take_photo) {
            mCameraSurfaceView.takePicture();
        } else if (i == R.id.camera_change) {
            mCameraSurfaceView.changeCamera();
        } else if (i == R.id.take_photo_back) {
            finish();
        }
    }


    @Override
    public void autoFocus() {
        mCameraSurfaceView.setAutoFocus();
    }

    @Override
    public void takePhotoComplete(String imgPath) {
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra(IMAGE_PATH, imgPath);
        //设置返回数据
        CameraActivity.this.setResult(RESULT_OK, intent);
        finish();
    }
}
