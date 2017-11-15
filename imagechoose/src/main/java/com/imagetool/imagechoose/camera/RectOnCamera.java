package com.imagetool.imagechoose.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 类名称：RectOnCamera
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/11/14
 * 描述：放在图片上的选中框
 */
public class RectOnCamera extends View {
    private static final String TAG = "CameraSurfaceView";
    private int mScreenWidth;
    private int mScreenHeight;
    private Paint mPaint;
    private RectF mRectF;
    // 圆
    private Point centerPoint;
    private int radio;
    private float density;
    private boolean showFouceCircle = false;
    private boolean showRect = true;

    public RectOnCamera(Context context) {
        this(context, null);
    }

    public RectOnCamera(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectOnCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenMetrix(context);
        initView(context);
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    public void setShowRect(boolean showRect) {
        this.showRect = showRect;
    }

    private void initView(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 防抖动
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

        density = context.getResources().getDisplayMetrics().density;
        int width = (int) (mScreenWidth*0.6);
        int heigh = width;
        int marginLeft = (mScreenWidth - width) / 2;
        int marginTop = (mScreenHeight - heigh) / 2;
        mRectF = new RectF(marginLeft, marginTop - 20*density, mScreenWidth - marginLeft, mScreenHeight - marginTop - 20*density);

        centerPoint = new Point(mScreenWidth/2, (int) (mScreenHeight/2 - 20*density));
        radio = (int) (mScreenWidth*0.07);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showRect){
            mPaint.setColor(Color.RED);
            canvas.drawRect(mRectF, mPaint);
        }

        Log.i(TAG, "onDraw");
        if (showFouceCircle){
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(centerPoint.x,centerPoint.y, radio,mPaint);// 外圆
            canvas.drawCircle(centerPoint.x,centerPoint.y, radio - 20,mPaint); // 内圆
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                centerPoint = new Point(x, y);
                invalidate();
                if (mIAutoFocus != null){
                    mIAutoFocus.autoFocus();
                }
                return true;
        }
        return true;
    }

    private IAutoFocus mIAutoFocus;

    /** 聚焦的回调接口 */
    public interface  IAutoFocus{
        void autoFocus();
    }

    public void setIAutoFocus(IAutoFocus mIAutoFocus) {
        this.mIAutoFocus = mIAutoFocus;
    }
}
