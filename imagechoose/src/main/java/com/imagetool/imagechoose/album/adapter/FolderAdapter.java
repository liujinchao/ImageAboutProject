package com.imagetool.imagechoose.album.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.imagetool.imagechoose.ImageChooseConstant;
import com.imagetool.imagechoose.R;
import com.imagetool.imagechoose.albumBean.ImageInfo;
import com.imagetool.imagechoose.res.IChooseDrawable;

import java.util.ArrayList;

/**
 * 类名称：FolderAdapter
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/20 15:00
 * 描述：TODO
 */
public class FolderAdapter extends BaseAdapter {
    private IChooseDrawable drawable;
    private Fragment fragment;
    public ArrayList<ImageInfo> data;

    private final int TYPE_DEFAULT = 0;//没有拍照功能模块
    private final int TYPE_TAKEPIC = 1;//有拍照功能模块

    private boolean isTakePhoto = ImageChooseConstant.takePhotoType != ImageChooseConstant.TP_NONE;

    public FolderAdapter(Fragment fragment, ArrayList<ImageInfo> data, IChooseDrawable drawable){
        this.fragment=fragment;
        this.data=data;
        this.drawable=drawable;
    }

    public void setChooseDrawable(IChooseDrawable drawable){
        this.drawable=drawable;
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && isTakePhoto){
            return TYPE_TAKEPIC;
        }
        return TYPE_DEFAULT;
    }

    public boolean isTakePhoto(int position){
        return getItemViewType(position) == TYPE_TAKEPIC;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position) == TYPE_TAKEPIC){
            convertView = LayoutInflater.from(fragment.getContext()).inflate(R.layout.image_chooser_item_camera,parent,false);
            if(ImageChooseConstant.tackPhotoIcon>0){
                ((ImageView)convertView).setImageResource(ImageChooseConstant.tackPhotoIcon);
            }
        }else{
            ImageHolder holder;
            if(convertView == null||convertView.getTag()==null){
                convertView= LayoutInflater.from(fragment.getContext()).inflate(R.layout.image_chooser_item_image,parent,false);
//                holder=new ImageHolder(convertView);
                holder = new ImageHolder();
                holder.mImage= (ImageView) convertView.findViewById(R.id.mImage);
                holder.mFlag = convertView.findViewById(R.id.mFlag);
                convertView.setTag(holder);
            }else{
                holder = (ImageHolder) convertView.getTag();
            }
            initData(data.get(position),holder.mImage,holder.mFlag);
        }
        return convertView;
    }

    private void initData(ImageInfo imageInfo,ImageView image,View vFlag) {
        //图片加载
        DrawableRequestBuilder r = Glide.with(fragment).load(imageInfo.path)
                .error(ImageChooseConstant.errorResId)
                .placeholder(ImageChooseConstant.placeResId);
        if(ImageChooseConstant.loadAnimateResId <= 0){
            r.dontAnimate();
        }else{
            r.animate(ImageChooseConstant.loadAnimateResId);
        }
        r.into(image);

        //状态加载
        if(drawable!=null){
            if(imageInfo.positon <= 0){
                image.setColorFilter(ImageChooseConstant.unChooseFilter);
            }else{
                image.setColorFilter(ImageChooseConstant.chooseFilter);
            }
            setBg(vFlag,drawable.get(imageInfo.positon));
        }
    }
    private void setBg(View v, Drawable drawable){
        v.setBackgroundDrawable(null);
        v.setBackgroundDrawable(drawable);
    }

    private static class ImageHolder{
        ImageView mImage;
        View mFlag;
    }

}
