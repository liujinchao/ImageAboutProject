package com.imagetool.imagechoose.album;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;


import com.imagetool.imagechoose.ImageChooseConstant;
import com.imagetool.imagechoose.albumBean.ImageFolder;
import com.imagetool.imagechoose.albumBean.ImageInfo;
import com.imagetool.imagechoose.callBack.IAlbumClickListener;
import com.imagetool.imagechoose.callBack.IImageClickListener;
import com.imagetool.imagechoose.res.IChooseDrawable;
import com.imagetool.utils.ImageChooseUtil;
import com.imagetool.utils.LogUtil;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：AlbumEntry
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/18 14:15
 * 描述：图片选择模块
 */
public class AlbumEntry extends AbsAlbumEntry implements AlbumTool.Callback,IAlbumClickListener,IImageClickListener {
    private FragmentActivity activity;
    private int containerId;
    private IFolderShower folderShower;
    private IAlbumShower albumShower;

    private Intent actIntent;

    private AlbumTool tool;

    private final int REQ_CROP = 0x10;

    public AlbumEntry(FragmentActivity activity, int containerId, IFolderShower fShower, IAlbumShower aShower){
        this.activity=activity;
        this.containerId=containerId;
        this.folderShower=fShower;
        this.albumShower=aShower;
        init();
    }

    private void init(){
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(containerId,folderShower.getFragment())
                .commitAllowingStateLoss();
        albumShower.setAlbumClickListener(this);
        folderShower.setImageClickListener(this);
        tool=new AlbumTool(activity);
        tool.setCallback(this);
        tool.findAlbumsAsync();

        actIntent=activity.getIntent();
        setCrop(actIntent.getBooleanExtra(ImageChooseConstant.INTENT_IS_CROP,false));
        if(isCrop()){
            setMax(1);
        }else{
            setMax(actIntent.getIntExtra(ImageChooseConstant.INTENT_MAX_IMG,getMax()));
        }
        if(getMax()==1){
            this.folderShower.setChooseDrawable(null);
        }else{
            this.folderShower.setChooseDrawable(ImageChooseConstant.chooseDrawable);
        }
    }

    public void refreshData(){
        tool.findAlbumsAsync();
    }

    //显示相册选择器
    public void showAlbumChooser(){
        LogUtil.d("显示相册选择器");
        albumShower.show();
    }

    //关闭相册选择器
    public void cancelAlbumChooser(){
        albumShower.cancel();
    }

    @Override
    public void onFolderFinish(ImageFolder folder) {
        LogUtil.d("图片集《"+folder.getName()+"》查找完毕");
        folderShower.setFolder(folder);
    }

    @Override
    public void onAlbumFinish(ArrayList<ImageFolder> albums) {
        LogUtil.d("相册查找完毕,共"+albums.size()+"个相册");
        albumShower.setAlbums(albums);
    }

    @Override
    public void onAlbumClick(ImageFolder folder) {
        tool.findFolderAsync(folder);
        cancelAlbumChooser();
    }

    public void crop(String path){
        int shape = actIntent.getIntExtra(ImageChooseConstant.INTENT_CROP_TYPE,0);
        Intent intent=new Intent(ImageChooseConstant.ACTION_CROP);
        intent.putExtra(ImageChooseConstant.INTENT_CROP_DATA,path);
        intent.putExtra(ImageChooseConstant.INTENT_CROP_TYPE,shape);
        if(shape == 0){
            String desUrl = ImageChooseUtil.startUCrop(activity,path,1000,
                    actIntent.getIntExtra(ImageChooseConstant.INTENT_CROP_WIDTH,100),
                    actIntent.getIntExtra(ImageChooseConstant.INTENT_CROP_HEIGHT,100));
            Log.d("TAG","desUrl:"+desUrl);
        }else if (shape == ImageChooseConstant.TP_CROPE_CUSTOM) {
            intent.putExtra(ImageChooseConstant.INTENT_CROP_COVER,actIntent.getStringExtra(ImageChooseConstant.INTENT_CROP_COVER));
            intent.putExtra(ImageChooseConstant.INTENT_CROP_PARAM,actIntent.getIntExtra(ImageChooseConstant.INTENT_CROP_PARAM,0));
            activity.startActivityForResult(intent,REQ_CROP);
        }else {
                intent.putExtra(ImageChooseConstant.INTENT_CROP_WIDTH,actIntent.getIntExtra(ImageChooseConstant.INTENT_CROP_WIDTH,500));
                intent.putExtra(ImageChooseConstant.INTENT_CROP_HEIGHT,actIntent.getIntExtra(ImageChooseConstant.INTENT_CROP_HEIGHT,500));
                activity.startActivityForResult(intent,REQ_CROP);
        }
    }

    @Override
    public boolean onAddSelect(List<ImageInfo> data, ImageInfo info) {
        if(getMax()==1){
            if(isCrop()){
                crop(info.path);
            }else{
                Intent intent=new Intent();
                ArrayList<String> result=new ArrayList<>();
                result.add(info.path);
                intent.putExtra(ImageChooseConstant.RESULT_DATA_IMG,result);
                activity.setResult(Activity.RESULT_OK,intent);
                activity.finish();
            }
            return true;
        }else{
            if(data.size()==getMax()){
                Toast.makeText(activity,"最多只能选择"+getMax()+"张", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void chooseFinish(){
        List<ImageInfo> res = folderShower.getSelectedImages();
        int resSize = res.size();
        if(resSize>0){
            ArrayList<String> data=new ArrayList<>(resSize);
            for (int i=0;i<resSize;i++){
                data.add(res.get(i).path);
            }
            chooseFinish(data);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REQ_CROP){
                chooseFinish(data.getStringExtra(ImageChooseConstant.RESULT_DATA_IMG));
            }else if (requestCode == 1000) {
                final Uri resultUri = UCrop.getOutput(data);
                Log.d("TAG",resultUri.getPath());
                chooseFinish(resultUri.getPath());
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
            }
        }
    }

    private void chooseFinish(ArrayList<String> data){
        if(data.size()>0){
            Intent intent=new Intent();
            intent.putExtra(ImageChooseConstant.RESULT_DATA_IMG,data);
            activity.setResult(Activity.RESULT_OK,intent);
            activity.finish();
        }
    }

    private void chooseFinish(String data){
        if(data!=null){
            ArrayList<String> c=new ArrayList<>(1);
            c.add(data);
            chooseFinish(c);
        }
    }

    @Override
    public boolean onCancelSelect(List<ImageInfo> data, ImageInfo info) {
        return false;
    }

    public interface IAlbumShower{
        void setAlbums(ArrayList<ImageFolder> albums);
        void setAlbumClickListener(IAlbumClickListener listener);
        void show();
        void cancel();
    }

    public interface IFolderShower{
        void setChooseDrawable(IChooseDrawable drawable);
        void setFolder(ImageFolder folder);
        void setImageClickListener(IImageClickListener listener);
        List<ImageInfo> getSelectedImages();
        Fragment getFragment();
    }

}
