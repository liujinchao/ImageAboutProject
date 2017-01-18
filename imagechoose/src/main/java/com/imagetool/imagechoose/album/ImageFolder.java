package com.imagetool.imagechoose.album;

import java.util.ArrayList;

/**
 * 类名称：ImageFolder
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/18 14:35
 * 描述：相册文件信息
 */
public class ImageFolder {
    private String dir;                    //文件路径
    private String firstImagePath;        //第一张图片的路径
    private String name;                //文件夹显示名称
    private int count;                    //图片的数量
    private String id;                    //文件夹在数据库中的id
    private ArrayList<ImageInfo> datas;    //文件夹中图片

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf + 1);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDatas(ArrayList<ImageInfo> datas){
        this.datas=datas;
    }

    public ArrayList<ImageInfo> getDatas(){
        return datas;
    }

}