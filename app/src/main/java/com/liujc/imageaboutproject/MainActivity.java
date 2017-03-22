package com.liujc.imageaboutproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.imagetool.imagechoose.ImageChooseConstant;
import com.imagetool.imagechoose.crop.CropPath;
import com.imagetool.utils.LogUtil;
import com.liujc.largerimagescaleview.ImageSource;
import com.liujc.largerimagescaleview.LargerImageScaleView;
import com.liujc.largerimagescaleview.activity.BasicFeaturesActivity;
import com.liujc.largerimagescaleview.activity.viewpager.ViewPagerActivity;


public class MainActivity extends AppCompatActivity {

    private GridView mGrid;
    private ShowAdapter adapter;
    private boolean flag;       //多选flag
    LargerImageScaleView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGrid= (GridView) findViewById(R.id.mGrid);
        mGrid.setAdapter(adapter=new ShowAdapter());
        initData();
    }

    private void initData() {
        imageView = (LargerImageScaleView)findViewById(R.id.imageView);
//        imageView.setImage(ImageSource.asset("squirrel.jpg"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        flag=false;
        Intent intent=new Intent(ImageChooseConstant.ACTION_ALBUM);
//        intent.putExtra(ImageChooseConstant.INTENT_TAKE_PHOTO_TYPE,ImageChooseConstant.TP_CUSTOM);
        switch (item.getItemId()){
            case R.id.mOne:
                intent.putExtra(ImageChooseConstant.INTENT_MAX_IMG,1);
                intent.putExtra(ImageChooseConstant.INTENT_TAKE_PHOTO_TYPE,ImageChooseConstant.TP_CUSTOM);
                startActivityForResult(intent,1);
                break;
            case R.id.mNine:
                intent.putExtra(ImageChooseConstant.INTENT_MAX_IMG,9);
                intent.putExtra(ImageChooseConstant.INTENT_TAKE_PHOTO_TYPE,ImageChooseConstant.TP_SYSTEM);
                if(flag){
                    intent.putStringArrayListExtra(ImageChooseConstant.INTENT_EXIST_DATA,adapter.data);
                }
                flag=true;
                startActivityForResult(intent,1);
                break;
            case R.id.mCustomCrop:
                intent.putExtra(ImageChooseConstant.INTENT_IS_CROP,true);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_WIDTH,100);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_HEIGHT,100);
                startActivityForResult(intent,1);
                break;
            case R.id.mCrop:
                intent.putExtra(ImageChooseConstant.INTENT_IS_CROP,true);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_TYPE, ImageChooseConstant.TP_CROPE_CIRCLE);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_WIDTH,600);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_HEIGHT,600);
                startActivityForResult(intent,1);
                break;
            case R.id.mCrop2:
                intent.putExtra(ImageChooseConstant.INTENT_IS_CROP,true);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_TYPE, ImageChooseConstant.TP_CROPE_RECT);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_WIDTH,600);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_HEIGHT,600);
                startActivityForResult(intent,1);
                break;
            case R.id.mCrop3:
                LogUtil.d(ExampleCropPath.class.getName());
                intent.putExtra(ImageChooseConstant.INTENT_IS_CROP,true);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_TYPE, ImageChooseConstant.TP_CROPE_CUSTOM);
                intent.putExtra(ImageChooseConstant.INTENT_CROP_COVER,ExampleCropPath.class.getName());
                intent.putExtra(ImageChooseConstant.INTENT_CROP_PARAM,1);
                startActivityForResult(intent,1);
                break;
            case R.id.mNineGridImageView:
                Intent intent2=new Intent(MainActivity.this,NineGridImageViewActivity.class);
                startActivity(intent2);
                break;
            case R.id.larger_image_view:
                Intent intent3=new Intent(MainActivity.this,ViewPagerActivity.class);
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (data.getStringArrayListExtra(ImageChooseConstant.RESULT_DATA_IMG) != null
                    && data.getStringArrayListExtra(ImageChooseConstant.RESULT_DATA_IMG).size() == 1){
                imageView.setVisibility(View.VISIBLE);
                imageView.setImage(ImageSource.uri(data.getStringArrayListExtra(ImageChooseConstant.RESULT_DATA_IMG).get(0).toString()));
                return;
            }
            adapter.data.clear();
            adapter.data.addAll(data.getStringArrayListExtra(ImageChooseConstant.RESULT_DATA_IMG));
            adapter.notifyDataSetChanged();
            LogUtil.d("TAG:"+data.getStringArrayListExtra(ImageChooseConstant.RESULT_DATA_IMG).get(0).toString());
        }
    }
}
