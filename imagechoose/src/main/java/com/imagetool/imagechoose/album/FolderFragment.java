package com.imagetool.imagechoose.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.imagetool.imagechoose.ImageChooseConstant;
import com.imagetool.imagechoose.R;
import com.imagetool.imagechoose.album.adapter.FolderAdapter;
import com.imagetool.imagechoose.albumBean.ImageFolder;
import com.imagetool.imagechoose.albumBean.ImageInfo;
import com.imagetool.imagechoose.callBack.IAlpha;
import com.imagetool.imagechoose.callBack.IImageClickListener;
import com.imagetool.imagechoose.callBack.IPhotoCamera;
import com.imagetool.imagechoose.res.IChooseDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 类名称：FolderFragment
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/20 14:59
 * 描述：图片选择界面
 */
public class FolderFragment extends Fragment implements AlbumEntry.IFolderShower,IAlpha {

    private ViewGroup rootView;
    private GridView mGrid;
    private View mCover;
    private FolderAdapter adapter;
    private ArrayList<ImageInfo> data=new ArrayList<>();
    private Vector<ImageInfo> selectImgs;
    private IImageClickListener listener;
    private IChooseDrawable drawable;
    private IPhotoCamera photoShoot;

    private List<String> initSelect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView= (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.image_chooser_fragment_album,container,false);
            initView();
            initData();
        }
        return rootView;
    }

    private void initView(){
        mGrid= (GridView) rootView.findViewById(R.id.mAlbum);
        mGrid.setNumColumns(ImageChooseConstant.NUM_COLUMNS);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.isTakePhoto(position)){
                    if(photoShoot!=null){
                        photoShoot.takePhoto();
                    }
                }else{
                    ImageInfo info = data.get(position);
                    if(info.positon > 0){   //点击已经选择过得
                        boolean isCancelSuccess = listener.onCancelSelect(selectImgs,info);
                        if(!isCancelSuccess){
                            info.positon=0;
                            selectImgs.removeElement(info);
                            int size=selectImgs.size();
                            for(int i=0;i<size;i++){
                                selectImgs.get(i).positon=i+1;
                            }
                        }
                    }else{
                        boolean isSelectSuccess = listener.onAddSelect(selectImgs,info);
                        if(!isSelectSuccess){
                            info.positon=selectImgs.size()+1;
                            selectImgs.add(info);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mCover=rootView.findViewById(R.id.mCover);
    }

    public void setPhotoShoot(IPhotoCamera photoShoot){
        this.photoShoot=photoShoot;
    }

    public void setSelectImgs(List<String> data){
        initSelect=data;
    }

    private void initData(){
        adapter = new FolderAdapter(this,data,drawable);
        mGrid.setAdapter(adapter);
        selectImgs=new Vector<>();
        if(initSelect!=null){
            int size=initSelect.size();
            for (int i=0;i<size;i++){
                ImageInfo info=new ImageInfo();
                info.path=initSelect.get(i);
                info.positon=i+1;
                selectImgs.add(info);
            }
        }
    }

    @Override
    public void setChooseDrawable(IChooseDrawable drawable) {
        this.drawable=drawable;
        if(adapter!=null){
            adapter.setChooseDrawable(drawable);
        }
    }

    @Override
    public void setFolder(ImageFolder folder) {
        if(data!=null){
            data.clear();
            if(ImageChooseConstant.takePhotoType != ImageChooseConstant.TP_NONE){
                data.add(new ImageInfo());
            }
            data.addAll(folder.getDatas());
            int size=data.size();
            for (ImageInfo s:selectImgs){
                for(int i=0;i<size;i++){
                    ImageInfo info=data.get(i);
                    if(info.positon==0&&info.path!=null&&info.path.equals(s.path)){
                        data.remove(i);
                        data.add(i,s);
                        break;
                    }
                }
            }
        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ImageChooseConstant.chooseDrawable!=null){
            ImageChooseConstant.chooseDrawable.clear();
        }
    }

    @Override
    public void setImageClickListener(IImageClickListener listener) {
        this.listener=listener;
    }

    @Override
    public List<ImageInfo> getSelectedImages() {
        return selectImgs;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void setAlpha(int alpha) {
        mCover.setBackgroundColor(alpha);
    }

    @Override
    public void show() {
        mCover.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        mCover.setVisibility(View.GONE);
    }
}
