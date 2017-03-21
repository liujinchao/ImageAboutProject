package com.liujc.imageaboutproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import com.imagetool.utils.ImageLoaderManager;
import com.imagetool.widget.ninegridimageview.NineGridImageView;
import com.imagetool.widget.ninegridimageview.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class NineGridImageViewActivity extends AppCompatActivity {

    private NineGridImageView mNgiv1, mNgiv2, mNgiv3, mNgiv4, mNgiv5, mNgiv6, mNgiv7, mNgiv8, mNgiv9;
    private List<String> mData1, mData2, mData3, mData4, mData5, mData6, mData7, mData8, mData9;
    private String[] IMG_URL_ARR = {
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_grid_imageview);
        initView();
        initData();
    }

    private void initView() {
        mNgiv1 = (NineGridImageView) findViewById(R.id.ngiv1);
        mNgiv2 = (NineGridImageView) findViewById(R.id.ngiv2);
        mNgiv3 = (NineGridImageView) findViewById(R.id.ngiv3);
        mNgiv4 = (NineGridImageView) findViewById(R.id.ngiv4);
        mNgiv5 = (NineGridImageView) findViewById(R.id.ngiv5);
        mNgiv6 = (NineGridImageView) findViewById(R.id.ngiv6);
        mNgiv7 = (NineGridImageView) findViewById(R.id.ngiv7);
        mNgiv8 = (NineGridImageView) findViewById(R.id.ngiv8);
        mNgiv9 = (NineGridImageView) findViewById(R.id.ngiv9);
    }

    private void initData() {
        mData1 = new ArrayList<>(1);
        mData2 = new ArrayList<>(2);
        mData3 = new ArrayList<>(3);
        mData4 = new ArrayList<>(4);
        mData5 = new ArrayList<>(5);
        mData6 = new ArrayList<>(6);
        mData7 = new ArrayList<>(7);
        mData8 = new ArrayList<>(8);
        mData9 = new ArrayList<>(9);

        for (int i = 0; i < 1; i++) {
            mData1.add(IMG_URL_ARR[i]);
        }
        for (int i = 0; i < 2; i++) {
            mData2.add(IMG_URL_ARR[i]);
        }
        for (int i = 0; i < 3; i++) {
            mData3.add(IMG_URL_ARR[i]);
        }
        for (int i = 0; i < 4; i++) {
            mData4.add(IMG_URL_ARR[i]);
        }
        for (int i = 0; i < 5; i++) {
            mData5.add(IMG_URL_ARR[i]);
        }
        for (int i = 0; i < 6; i++) {
            mData6.add(IMG_URL_ARR[i]);
        }
        for (int i = 0; i < 7; i++) {
            mData7.add(IMG_URL_ARR[i]);
        }
        for (int i = 0; i < 8; i++) {
            mData8.add(IMG_URL_ARR[i]);
        }
        for (int i = 0; i < 9; i++) {
            mData9.add(IMG_URL_ARR[i]);
        }

        NineGridImageViewAdapter adapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                ImageLoaderManager.LoadNetImage(context,s, imageView);
            }

            //重写该方法自定义生成ImageView方式，用于九宫格头像中的一个个图片控件，可以设置ScaleType等属性
            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }
        };

        mNgiv1.setAdapter(adapter);
        mNgiv1.setImagesData(mData1);
        mNgiv2.setAdapter(adapter);
        mNgiv2.setImagesData(mData2);
        mNgiv3.setAdapter(adapter);
        mNgiv3.setImagesData(mData3);
        mNgiv4.setAdapter(adapter);
        mNgiv4.setImagesData(mData4);
        mNgiv5.setAdapter(adapter);
        mNgiv5.setImagesData(mData5);
        mNgiv6.setAdapter(adapter);
        mNgiv6.setImagesData(mData6);
        mNgiv7.setAdapter(adapter);
        mNgiv7.setImagesData(mData7);
        mNgiv8.setAdapter(adapter);
        mNgiv8.setImagesData(mData8);
        mNgiv9.setAdapter(adapter);
        mNgiv9.setImagesData(mData9);

    }
}
