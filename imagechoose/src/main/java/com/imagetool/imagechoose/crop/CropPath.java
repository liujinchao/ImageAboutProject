package com.imagetool.imagechoose.crop;

import android.graphics.Path;
import android.graphics.Rect;


public interface CropPath {
    //移动缩放操作范围
    Rect limit();
    //最终裁剪形状
    Path path();

}
