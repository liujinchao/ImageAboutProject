package com.imagetool.imagechoose;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.imagetool.imagechoose.album.AlbumEntry;
import com.imagetool.imagechoose.album.AlbumPopup;
import com.imagetool.imagechoose.album.FolderFragment;
import com.imagetool.imagechoose.albumBean.ImageFolder;
import com.imagetool.imagechoose.albumBean.ImageInfo;
import com.imagetool.imagechoose.callBack.IPhotoCamera;
import com.imagetool.imagechoose.camera.CameraActivity;
import com.imagetool.imagechoose.edit.ImageEditActivity;
import com.imagetool.utils.ImageChooseUtil;
import com.imagetool.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.imagetool.imagechoose.ImageChooseConstant.SHOW_IMG_EDIT;
import static com.imagetool.imagechoose.edit.ImageEditActivity.PATH;

/**
 * 类名称：ChoosePictureEntryActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/20 14:34
 * 描述：图片选择入口
 */
public class ChoosePictureEntryActivity extends FragmentActivity implements IPhotoCamera {

    private AlbumEntry entry;
    private Toolbar toolbar;
    private MenuItem mSure;

    private static final int REQ_TAKE_PIC_FROM_DEFAULT= 0x15; // 从默认系统相机拍摄
    private static final int REQ_TAKE_PIC_FROM_CUSTOM = 0x16; // 从自定义相机拍摄
    private final static int ACTIVITY_CODE_EDIT_PHOTO = 0x17; // 编辑图片

    private boolean showImgEdit = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser_activity_entry);
        setTitle();
        initView();
    }

    private void initView() {
        if (getIntent() != null && getIntent().getExtras() != null){
            showImgEdit = getIntent().getExtras().getBoolean(SHOW_IMG_EDIT,false);
        }
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        ImageChooseConstant.albumPopupHeight = (int) (outRect.height()*0.6f);
        FolderFragment m = new FolderFragment();
        m.setPhotoShoot(this);
        m.setSelectImgs(getIntent().getStringArrayListExtra(ImageChooseConstant.INTENT_EXIST_DATA));
        entry = new AlbumEntry(this, R.id.mEntry, m, new AlbumPopup(this,toolbar,m)){
            @Override
            public void onAlbumClick(ImageFolder folder) {
                super.onAlbumClick(folder);
                toolbar.setTitle(folder.getName()+"("+folder.getCount()+")");
            }

            @Override
            public boolean onAddSelect(List<ImageInfo> data, ImageInfo info) {
                boolean a = super.onAddSelect(data, info);
                if(!a && mSure!=null && getMax()!=1){
                    mSure.setEnabled(true);
                }
                return a;
            }

            @Override
            public boolean onCancelSelect(List<ImageInfo> data, ImageInfo info) {
                boolean a=super.onCancelSelect(data, info);
                if(!a&&mSure!=null&&data.size()<=1){
                    mSure.setEnabled(false);
                }
                return a;
            }
        };

        if(entry.getMax()>1){
            mSure=toolbar.getMenu().getItem(1);
            mSure.setEnabled(false);
        }else{
            toolbar.getMenu().getItem(1).setVisible(false);
        }

        ImageChooseConstant.takePhotoType = getIntent().getIntExtra(ImageChooseConstant.INTENT_TAKE_PHOTO_TYPE,
                ImageChooseConstant.TP_SYSTEM);
//        IntentFilter filter=new IntentFilter(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        registerReceiver(receiver,filter);
    }

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            entry.refreshData();
        }
    };

    private void setTitle(){
        toolbar= (Toolbar) findViewById(R.id.mTitle);
        toolbar.setBackgroundColor(getResources().getColor(R.color.title_bar_background_color));
        toolbar.setTitle("图片选择");
        toolbar.setNavigationIcon(R.drawable.image_choose_back);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.inflateMenu(R.menu.menu_album);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.mAlbum) {
                    entry.showAlbumChooser();
                }else if(i==R.id.mSure){
                    entry.chooseFinish();
                }
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("result-"+resultCode+"/"+requestCode);
        entry.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode == REQ_TAKE_PIC_FROM_DEFAULT){ //调用系统相机拍照的回调
                if (showImgEdit){
                    jumpToImgEdit();
                }else {
                    setResultBackToSource(fileName);
                }
            }else if (requestCode == REQ_TAKE_PIC_FROM_CUSTOM){//调用自定义相机拍照的回调
                if (showImgEdit){
                    jumpToImgEdit();
                }else {
                    setResultBackToSource(fileName);
                }
            }else if (requestCode == ACTIVITY_CODE_EDIT_PHOTO){//调用图片编辑后的回调
                setResultBackToSource(fileName);
            }
        }else if (resultCode == -2) { // 重新拍照
            Intent intent = new Intent(ChoosePictureEntryActivity.this,CameraActivity.class);
            intent.putExtra(CameraActivity.IMAGE_PATH,fileName);
            if (ImageChooseConstant.takePhotoType == ImageChooseConstant.TP_SYSTEM){
                startActivityForResult(intent,REQ_TAKE_PIC_FROM_DEFAULT);
            }else {
                startActivityForResult(intent,REQ_TAKE_PIC_FROM_CUSTOM);
            }
        }
    }

    private void jumpToImgEdit(){
        Intent intent = new Intent(ChoosePictureEntryActivity.this, ImageEditActivity.class);
        intent.putExtra(PATH, fileName);
        startActivityForResult(intent,ACTIVITY_CODE_EDIT_PHOTO);
    }
    private void setResultBackToSource(String imgPath) {
        //让拍摄的图片可以在相册目录中出现
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(imgPath))));
        if(entry.isCrop()){
            entry.crop(imgPath);
        }else{
            Intent intent=new Intent();
            ArrayList<String> d=new ArrayList<>();
            d.add(imgPath);
            intent.putExtra(ImageChooseConstant.RESULT_DATA_IMG,d);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }

    @Override
    public void takePhoto() {
        fileName = ImageChooseUtil.getPicturePath() + System.currentTimeMillis() + ".jpg";
        if(ImageChooseConstant.takePhotoType == ImageChooseConstant.TP_SYSTEM){
            doSystemDefaultCamera();
        }else {
            LogUtil.d("自定义相机");
            doCustomCamera();
        }
    }
    String fileName = "";
    private void doCustomCamera(){
        File mFile = new File(fileName);
        if (!mFile.getParentFile().exists())
            mFile.getParentFile().mkdirs();
        Intent intent = new Intent(ChoosePictureEntryActivity.this,CameraActivity.class);
        intent.putExtra(CameraActivity.IMAGE_PATH,fileName);
        intent.putExtra(ImageChooseConstant.SHOW_IMG_RECT,
                getIntent().getExtras().getBoolean(ImageChooseConstant.SHOW_IMG_RECT,false));
        startActivityForResult(intent,REQ_TAKE_PIC_FROM_CUSTOM);
    }
    private void doSystemDefaultCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //TODO 传入保存路径直接保存
        fileName = ImageChooseUtil.getPicturePath()+ System.currentTimeMillis()+".jpg";
        File folder = new File(ImageChooseUtil.getPicturePath());
        if(!folder.exists()){
            if(!folder.mkdirs()){
                Toast.makeText(this,"无法拍照", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        File file = new File(fileName);
        //获取系统版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.N) {
            // 从文件中创建uri
            Uri uri = Uri.fromFile(file);
            LogUtil.d("uri:"+uri.toString());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent,REQ_TAKE_PIC_FROM_DEFAULT);
    }

}
