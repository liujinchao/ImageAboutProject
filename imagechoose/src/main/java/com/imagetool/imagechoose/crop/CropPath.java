package com.imagetool.imagechoose.crop;

import android.graphics.Path;
import android.graphics.Rect;


public interface CropPath {

    int SHAPE_RECT=1;
    int SHAPE_CIRCLE=2;

    //移动缩放操作范围
    Rect limit();
    //最终裁剪形状
    Path path();

}
