package com.imagetool.imagechoose.callBack;


import com.imagetool.imagechoose.albumBean.ImageInfo;

import java.util.List;

/**
 * 类名称：IImageClickListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/20 14:09
 * 描述：TODO
 */
public interface IImageClickListener {
    //返回值为是否，默认会增加选中
    boolean onAddSelect(List<ImageInfo> data, ImageInfo info);
    //返回值为是否，默认会取消选中
    boolean onCancelSelect(List<ImageInfo> data, ImageInfo info);

}
