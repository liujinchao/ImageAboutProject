package com.imagetool.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.imagetool.imagechoose.R;

/**
 * 类名称：ImageLoaderManager
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/3/21 9:59
 * 描述：图片加载管理
 */
public class ImageLoaderManager {

    public static void LoadNetImage(Context context,String imgUrl, ImageView imageView) {
        Glide.with(context).load(imgUrl)
                .placeholder(R.drawable.ic_head_image_loading)
                .error(R.drawable.ic_head_image_error)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public static void LoadLocalImage(Context context,String path, ImageView imageView) {
        Glide.with(context).load(Uri.parse("file://" + path).toString())
                .placeholder(R.drawable.ic_head_image_loading)
                .error(R.drawable.ic_head_image_error)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
}