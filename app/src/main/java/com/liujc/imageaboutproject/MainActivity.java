package com.liujc.imageaboutproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.imagetool.imagechoose.IcFinal;
import com.imagetool.imagechoose.crop.CropPath;
import com.imagetool.utils.LogUtil;


public class MainActivity extends AppCompatActivity {

    private GridView mGrid;
    private ShowAdapter adapter;
    private boolean flag;       //多选flag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGrid= (GridView) findViewById(R.id.mGrid);
        mGrid.setAdapter(adapter=new ShowAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        flag=false;
        Intent intent=new Intent(IcFinal.ACTION_ALBUM);
        switch (item.getItemId()){
            case R.id.mOne:
                intent.putExtra(IcFinal.INTENT_MAX_IMG,1);
                break;
            case R.id.mNine:
                intent.putExtra(IcFinal.INTENT_MAX_IMG,9);
                if(flag){
                    intent.putStringArrayListExtra(IcFinal.INTENT_EXIST_DATA,adapter.data);
                }
                flag=true;
                break;
            case R.id.mCrop:
                intent.putExtra(IcFinal.INTENT_IS_CROP,true);
                intent.putExtra(IcFinal.INTENT_CROP_SHAPE, CropPath.SHAPE_CIRCLE);
                intent.putExtra(IcFinal.INTENT_CROP_WIDTH,500);
                intent.putExtra(IcFinal.INTENT_CROP_HEIGHT,500);
                break;
            case R.id.mCrop2:
                intent.putExtra(IcFinal.INTENT_IS_CROP,true);
                intent.putExtra(IcFinal.INTENT_CROP_SHAPE, CropPath.SHAPE_RECT);
                intent.putExtra(IcFinal.INTENT_CROP_WIDTH,500);
                intent.putExtra(IcFinal.INTENT_CROP_HEIGHT,500);
                break;
            case R.id.mCrop3:
                LogUtil.d(ExampleCropPath.class.getName());
                intent.putExtra(IcFinal.INTENT_IS_CROP,true);
                intent.putExtra(IcFinal.INTENT_CROP_COVER,ExampleCropPath.class.getName());
                intent.putExtra(IcFinal.INTENT_CROP_PARAM,1);
                break;
        }
        startActivityForResult(intent,1);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            adapter.data.clear();
            adapter.data.addAll(data.getStringArrayListExtra(IcFinal.RESULT_DATA_IMG));
            adapter.notifyDataSetChanged();
        }
    }
}
