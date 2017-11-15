package com.imagetool.imagechoose.edit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.imagetool.imagechoose.R;
import com.imagetool.imagechoose.edit.paint.PaintConfig;
import com.imagetool.imagechoose.edit.paint.PaintView;

public class ImageEditActivity extends AppCompatActivity {

    public static final String PATH = "path";
    public static final int GUIDE_WIN_OFFSET = 80;
    boolean isShare = false;
    private RadioGroup shapeGroup;
    private Button btUndo, btColor, btWidth, btCapture, btShare, btText;
    private ImageButton btClear;
    private PaintView paintView;
    private PopupWindow mColPopup, mWidthPopup, mPopupTips;
    private View rootView;

    private Dialog mProgressDialog;

    private String filePath = null;

    private float density;
    private int mScreenWidth;
    private int mScreenHeight;
    private TextView tvArtificalRemarks;
    private boolean flag = true;
    private float scaleSize = (float) 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        if (Build.VERSION.SDK_INT < 16) {
            // Hide the status bar
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ActionBar ab = getSupportActionBar();
            if( ab != null ) {
                ab.hide();
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            ActionBar ab = getSupportActionBar();
            if( ab != null ) {
                ab.hide();
            }
        }
        filePath = getIntent().getStringExtra(PATH);
        getScreenMetrix(this);
        initView();
        initEvent();
        initData();
        initShow();
    }

    private void initShow() {
        if (flag){
            tvArtificalRemarks.setVisibility(View.VISIBLE);
            shapeGroup.setVisibility(View.GONE);
            PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Rect);
            density = getResources().getDisplayMetrics().density;
            int width = (int) (mScreenWidth*0.6);
            if (scaleSize > 0){
                width = (int) (width * 0.9);
            }
            int marginLeft = (mScreenWidth - width) / 2;
            int marginTop = (mScreenHeight - width) / 2;
            paintView.initPaintRect(marginLeft, marginTop - 20*density, mScreenWidth - marginLeft, mScreenHeight - marginTop - 20*density);
        }else {
            tvArtificalRemarks.setVisibility(View.GONE);
            shapeGroup.setVisibility(View.VISIBLE);
        }
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private void showPopupTip(String tip) {
        TextView tipView = ((TextView) mPopupTips.getContentView().findViewById(R.id.txTips));
        tipView.setText(tip + "");
        mPopupTips.showAtLocation(rootView, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, GUIDE_WIN_OFFSET);
        tipView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPopupTips != null && mPopupTips.isShowing()) {
                    mPopupTips.dismiss();
                }
            }
        }, 3000);
    }

    private void initData() {
        switch (getIndex((RadioGroup) mColPopup.getContentView().findViewById(R.id.colRG))) {
            case 1:
                PaintConfig.getInstance().setCurrentShapeColor(getResources().getColor(R.color.col2));
                break;
            case 2:
                PaintConfig.getInstance().setCurrentShapeColor(getResources().getColor(R.color.col3));
                break;
            case 3:
                PaintConfig.getInstance().setCurrentShapeColor(getResources().getColor(R.color.col4));
                break;
            case 4:
                PaintConfig.getInstance().setCurrentShapeColor(getResources().getColor(R.color.col5));
                break;
            case 0:
            default:
                PaintConfig.getInstance().setCurrentShapeColor(getResources().getColor(R.color.col1));
                break;
        }
        switch (getIndex((RadioGroup) mWidthPopup.getContentView().findViewById(R.id.widthRG))) {
            case 1:
                PaintConfig.getInstance().setCurrentShapeWidth(getResources().getDimension(R.dimen.width2));
                break;
            case 2:
                PaintConfig.getInstance().setCurrentShapeWidth(getResources().getDimension(R.dimen.width3));
                break;
            case 3:
                PaintConfig.getInstance().setCurrentShapeWidth(getResources().getDimension(R.dimen.width4));
                break;
            case 4:
                PaintConfig.getInstance().setCurrentShapeWidth(getResources().getDimension(R.dimen.width5));
                break;
            case 0:
            default:
                PaintConfig.getInstance().setCurrentShapeWidth(getResources().getDimension(R.dimen.width1));
                break;
        }

    }

    private void initEvent() {

        RadioButton textRB = (RadioButton)findViewById( R.id.text );
        if( textRB != null ) {
            textRB.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Text);
                            showInputDialog();
                        }
                    }
            );
        }
        shapeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.line) {
                    PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Line);

                } else if (checkedId == R.id.circle) {
                    PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Circle);

                } else if (checkedId == R.id.rect) {
                    PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Rect);

                } else if (checkedId == R.id.text) {
//                     PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Text);
//                     showInputDialog();
                } else {
                    PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Point);

                }
            }
        });

        btUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.removeLastOne();
            }
        });
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.removeAll();
            }
        });
        btCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.capture();
            }
        });
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.capture();
                isShare = true;
            }
        });

        btColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColPopup.showAsDropDown(btColor, btColor.getWidth(), -btColor.getHeight() - 6);
            }
        });

        btWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWidthPopup.showAsDropDown(btWidth, btWidth.getWidth(), -btWidth.getHeight() - 6);
            }
        });

        mColPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                initData();
            }
        });
        mWidthPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                initData();
            }
        });

        TextView backTv = (TextView)findViewById( R.id.back_tv_id );
        if( backTv != null ) {
            backTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult( RESULT_CANCELED );
                    finish();
                }
            });
        }

        TextView retakeTv = (TextView)findViewById( R.id.retake_tv_id );
        if( retakeTv != null ) {
            retakeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult( -2 );
                    finish();
                }
            });
        }

        TextView okTv = (TextView)findViewById( R.id.ok_tv_id );
        if( okTv != null ) {
            okTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    new MyAsyncTask().execute( (String[]) null);
                }
            });
        }

        tvArtificalRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                paintView.removeAll();
                PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Point);
                initShow();
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, Integer>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            showProgressDialog();
        }
        @Override
        protected Integer doInBackground(String... params)
        {
            saveBitmap();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            dismissProgressDialog();
            finish();
        }
    }


    private Handler mySaveHandler = new Handler(new MyHandlerCallback());

    private class MyHandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            dismissProgressDialog();
            finish();
            return false;
        }
    }



    private class SaveThread extends Thread {

        @Override
        public void run() {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            saveBitmap();

            mySaveHandler.sendEmptyMessage(0);
        }
    }

    private void saveBitmap() {
        Bitmap src = null, dest = null;

        try {
            int degree = ImageDispose.readPictureDegree( filePath );

            Bitmap bitmap = BitmapFactory.decodeFile( filePath );

            int originWidth = bitmap.getWidth();
            int originHeight = bitmap.getHeight();
            Log.d("Save", "originWidth = " + originWidth + ", originHeight = " + originHeight );

            if( degree != 0 ) {
                src = ImageDispose.rotaingImageView(90, bitmap);
            } else {
                src = bitmap;
            }

            dest = paintView.captureIt();
            if( dest != null && src != null ) {

                int destWidth = dest.getWidth();
                int destHeight = dest.getHeight();
                Log.d("Save", "destWidth = " + destWidth + ", destHeight = " + destHeight );

                // Calc display area
                float scaleWidth = 1.0f*originWidth/destWidth;
                float scaleHeight = 1.0f*originHeight/destHeight;
                int x=0, y=0, width=0, height=0;

                if( scaleWidth > scaleHeight ) {
                    x = 0; width = destWidth;

                    int realHeight = (int)(destHeight*scaleHeight/scaleWidth);
                    y = ( destHeight - realHeight )/2;
                    height = realHeight;
                } else {
                    y = 0; height = destHeight;
                    int realWidth = (int)(destWidth*scaleWidth/scaleHeight);
                    x = ( destWidth - realWidth )/2;
                    width = realWidth;
                }

                Log.d( "Save", "x = " + x + ", y = " + y + ", width = " + width + ", height = " + height );

                Bitmap scaledBitmap = null;
                try{
                    scaledBitmap = Bitmap.createBitmap(dest, x, y, width, height, null, false);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                if( scaledBitmap != null ) {
                    dest = ImageDispose.zoomImage(scaledBitmap, src.getWidth(), src.getHeight());

                    if (dest != null) {
                        dest = ImageDispose.mergeBitmap(src, dest);

                        if (dest != null) {
                            ImageDispose.saveBitmap(dest, filePath);
                        }
                    }
                }
            }
        }catch (Exception e ) {
            e.printStackTrace();
        }

        return;
    }

    private void initView() {
        try {
            ImageView iv = (ImageView) findViewById( R.id.bg_iv_id );
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            int degree = ImageDispose.readPictureDegree( filePath );

            Bitmap bitmap = BitmapFactory.decodeFile( filePath );
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth=dm.widthPixels;

            if(bitmap.getWidth()<=screenWidth){
                if( degree != 0 ) {
                    bitmap = ImageDispose.rotaingImageView(degree, bitmap);
                }
                iv.setImageBitmap(bitmap);
            }else{
                scaleSize = (float) (1.0 * screenWidth/bitmap.getWidth());
                bitmap= Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight()*screenWidth/bitmap.getWidth(), true);
                if( degree != 0 ) {
                    bitmap = ImageDispose.rotaingImageView(degree, bitmap);
                }
                iv.setImageBitmap(bitmap);
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }

        rootView = findViewById(R.id.rootView);
        shapeGroup = (RadioGroup) findViewById(R.id.shape_group);
        btUndo = (Button) findViewById(R.id.cancel);
        btClear = (ImageButton) findViewById(R.id.clear);
        btColor = (Button) findViewById(R.id.color);
        btWidth = (Button) findViewById(R.id.width);
        btCapture = (Button) findViewById(R.id.capture);
        btShare = (Button) findViewById(R.id.share);
        paintView = (PaintView) findViewById(R.id.paint_view);

        tvArtificalRemarks = (TextView) findViewById(R.id.tv_artifical_remarks);

        //颜色选择弹窗
        LayoutInflater inflater = LayoutInflater.from(this);
        View popColLayout = inflater.inflate(R.layout.layout_col_popup, null);
        mColPopup = new PopupWindow(popColLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mColPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//不设置背景,无法响应 返回键 和 界面外点击事件;应该算是谷歌大神的BUG

        //粗细选择弹窗
        View popWidthLayout = inflater.inflate(R.layout.layout_width_popup, null);
        mWidthPopup = new PopupWindow(popWidthLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mWidthPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //弹窗提示
        View popupTip = inflater.inflate(R.layout.layout_popup_tip, null);
        mPopupTips = new PopupWindow(popupTip, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupTips.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupTips.setAnimationStyle(R.style.GuideWindowAnimation);
        mPopupTips.setTouchable(false);
        mPopupTips.setOutsideTouchable(false);

        PaintConfig.getInstance().setCurrentShape(PaintConfig.Shape.Point);
    }

    private int getIndex(RadioGroup radioGroup) {
        return radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
    }

    private void setIndex(RadioGroup radioGroup, int index) {
        radioGroup.check(radioGroup.getChildAt(index).getId());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    private void showInputDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.input_dialog_view, null);
        dialog.setView(layout);
        final EditText contentET = (EditText)layout.findViewById(R.id.content_ed_id);

        dialog.setMessage("请输入提示内容");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String content = contentET.getText().toString();
                PaintConfig.getInstance().setContent( content );
            }
        });

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    public void showProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        mProgressDialog = createLoadingDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {

        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = null;
        if(context != null){
            inflater = LayoutInflater.from(context);
        }else{
            inflater = LayoutInflater.from(context);
        }

        View v = inflater.inflate(R.layout.dialog_loading_layout, null);
        FrameLayout layout = (FrameLayout) v.findViewById(R.id.dialog_view);
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context,R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        return loadingDialog;
    }

}
