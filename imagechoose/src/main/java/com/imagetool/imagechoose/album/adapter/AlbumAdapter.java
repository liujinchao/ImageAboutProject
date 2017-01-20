package com.imagetool.imagechoose.album.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.imagetool.imagechoose.ImageChooseConstant;
import com.imagetool.imagechoose.R;
import com.imagetool.imagechoose.albumBean.ImageFolder;

import java.util.ArrayList;
import java.util.Locale;

public class AlbumAdapter extends BaseAdapter {

    private ArrayList<ImageFolder> data;
    private Context context;

    public AlbumAdapter(Context context, ArrayList<ImageFolder> data){
        this.context=context;
        this.data=data;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        FolderHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.image_chooser_item_album,parent,false);
            holder=new FolderHolder();
            holder.mImage = (ImageView) convertView.findViewById(R.id.mImage);
            holder.mInfo = (TextView) convertView.findViewById(R.id.mInfo);
            convertView.setTag(holder);
        }else{
            holder= (FolderHolder) convertView.getTag();
        }
       setData(data.get(position),holder);
        return convertView;
    }

    void setData(ImageFolder folder, FolderHolder holder){
        //图片加载
        DrawableRequestBuilder r= Glide.with(context).load(folder.getFirstImagePath())
                .error(ImageChooseConstant.errorResId)
                .placeholder(ImageChooseConstant.placeResId);
        if(ImageChooseConstant.loadAnimateResId <= 0){
            r.dontAnimate();
        }else{
            r.animate(ImageChooseConstant.loadAnimateResId);
        }
        r.into(holder.mImage);
        holder.mInfo.setText(String.format(Locale.CHINA,"%1$s（%2$d）",folder.getName(),folder.getCount()));
    }
    private static class FolderHolder{
        ImageView mImage;
        TextView mInfo;
    }
}
