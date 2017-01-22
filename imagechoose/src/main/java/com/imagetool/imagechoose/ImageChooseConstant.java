package com.imagetool.imagechoose;

import com.imagetool.imagechoose.res.CircleChooseDrawable;
import com.imagetool.imagechoose.res.IChooseDrawable;

/**
 * 类名称：ImageChooseConstant
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/20 14:09
 * 描述：ImageChoose类库所用到的常量
 */
public class ImageChooseConstant {

    public static final String INTENT_MAX_IMG = "select_image_max_num";
    public static final String INTENT_IS_CROP = "image_is_crop";
    public static final String INTENT_CROP_WIDTH = "image_crop_width";
    public static final String INTENT_CROP_HEIGHT = "image_crop_height";
    public static final String INTENT_CROP_TYPE = "image_crop_type";
    public static final String INTENT_CROP_DATA = "image_crop_data";
    public static final String INTENT_CROP_COVER = "image_crop_set";
    public static final String INTENT_CROP_PARAM = "image_crop_param";
    public static final String INTENT_EXIST_DATA = "image_exist_data";
    public static final String  INTENT_TAKE_PHOTO_TYPE = "take_photo_type";


    public static final String RESULT_DATA_IMG = "select_img_path";

    public static final String ACTION_ALBUM = "android.intent.action.imagetool.album";
    public static final String ACTION_CROP = "android.intent.action.imagetool.crop";

    public static final int TP_SYSTEM = 0; //带有系统拍照功能
    public static final int TP_CUSTOM = 1; //带有自定义拍照功能
    public static final int TP_NONE = 2; // 不带拍照功能

    public static final int TP_CROPE_UCROP = 0; // 自定义截图
    public static final int TP_CROPE_RECT = 1; //方形截图
    public static final int TP_CROPE_CIRCLE = 2; //圆形截图
    public static final int  TP_CROPE_CUSTOM = 3; // 第三方截图

    /**标题的背景颜色*/
    public static int TITLE_COLOR = 0xFF0dc6e2;

    /**图片选择页，每行显示数*/
    public static int NUM_COLUMNS = 3;

    /**图片加载失败的图片*/
    public static int errorResId = 0;

    /**图片加载的占位图片*/
    public static int placeResId = R.drawable.image_choose_placeholder;

    /**图片加载的动画*/
    public static int loadAnimateResId = 0;

    /**拍照设置*/
    public static int takePhotoType = TP_CUSTOM;

    public static int tackPhotoIcon = 0;

    /**选中图片的滤镜颜色*/
    public static int chooseFilter = 0x55000000;

    /**未被选中的图片的滤镜颜色*/
    public static int unChooseFilter = 0;

    /**最新的图片集合显示名字*/
    public static String newestAlbumName = "最新图片";

    /**最新图片集合的最大数量*/
    public static int newestAlbumSize = 100;

    public static int albumPopupHeight = 600;

    public static String tantoToast = "";

    /**照片选择指示器*/
    public static IChooseDrawable chooseDrawable = new CircleChooseDrawable(true,0xFF33a6b8);

//    /**临时文件存储位置*/
//    public static String baseFolder = Environment.getExternalStorageDirectory().getAbsolutePath()+"/imageChoose/";
}
