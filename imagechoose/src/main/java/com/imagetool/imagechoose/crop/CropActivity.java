package com.imagetool.imagechoose.crop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.imagetool.imagechoose.ImageChooseConstant;
import com.imagetool.imagechoose.R;
import com.imagetool.utils.LogUtil;

import java.io.IOException;

/**
 * Description:
 */
public class CropActivity extends FragmentActivity {

    private Toolbar toolbar;
    private CropFragment cropFragment;
    private int shape;
    private int width;
    private int height;
    private String drawableName;
    private int drawableParam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser_activity_crop);
        setTitle();
        initByIntent();
        LogUtil.d("initByIntent");
    }

    public void initByIntent(){
        Intent intent=getIntent();
        String data=intent.getStringExtra(ImageChooseConstant.INTENT_CROP_DATA);
        shape=intent.getIntExtra(ImageChooseConstant.INTENT_CROP_TYPE,0);
        if(shape == ImageChooseConstant.TP_CROPE_CUSTOM){
            drawableName= intent.getStringExtra(ImageChooseConstant.INTENT_CROP_COVER);
            drawableParam=intent.getIntExtra(ImageChooseConstant.INTENT_CROP_PARAM,0);
            cropFragment=CropFragment.newFragment(data,drawableName,drawableParam);
        }else if(shape==ImageChooseConstant.TP_CROPE_CIRCLE||shape==ImageChooseConstant.TP_CROPE_RECT){
            width=intent.getIntExtra(ImageChooseConstant.INTENT_CROP_WIDTH,400);
            height=intent.getIntExtra(ImageChooseConstant.INTENT_CROP_HEIGHT,400);
            cropFragment=CropFragment.newFragment(data,shape,width,height);
        }
        cropFragment.setOnReadyRunnable(new Runnable() {
            @Override
            public void run() {
                toolbar.getMenu().getItem(0).setEnabled(true);
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mContainer,cropFragment)
                .commit();
    }

    public void setTitle(){
        toolbar= (Toolbar) findViewById(R.id.mTitle);
        toolbar.setBackgroundColor(ImageChooseConstant.TITLE_COLOR);
        toolbar.setTitle("裁剪图片");
        toolbar.setNavigationIcon(R.drawable.image_choose_back);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.inflateMenu(R.menu.menu_crop);
        toolbar.getMenu().getItem(0).setEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.mSure){
                    String file=getFilesDir().getAbsolutePath()+"/temp.jpg";
                    try {
                        cropFragment.crop(file);
                        Intent intent=new Intent();
                        intent.putExtra(ImageChooseConstant.RESULT_DATA_IMG,file);
                        setResult(RESULT_OK,intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }
}
