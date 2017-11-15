package com.imagetool.imagechoose.edit.paint;

import android.graphics.Color;
import android.support.annotation.ColorInt;

public class PaintConfig {
    static PaintConfig instance;
    /**
     * 画布宽高
     */
    int surfaceWidth, surfaceHeight;
    /**
     * 当前选择的形状
     */
    int currentShape;
    /**
     * 当前选择的形状颜色
     */
    int currentShapeColor;
    /**
     * 当前选择的形状宽度
     */
    float currentShapeWidth;
    /**
     * 画布背景颜色
     */
    @ColorInt
    int surfaceBackgroundColor;

    /**
     * 字体大小
     */
    float fontSize;


    String content;

    private PaintConfig() {
        currentShape = Shape.Point;
        currentShapeColor = Color.parseColor("#F30623");
        currentShapeWidth = 2f;

        fontSize = 30f;
        surfaceBackgroundColor = Color.WHITE;
    }

    public static PaintConfig getInstance() {
        if (instance == null) {
            synchronized (PaintConfig.class) {
                if (instance == null) {
                    instance = new PaintConfig();
                }
            }
        }
        return instance;
    }

    public int getSurfaceWidth() {
        return surfaceWidth;
    }

    public void setSurfaceWidth(int surfaceWidth) {
        this.surfaceWidth = surfaceWidth;
    }

    public int getSurfaceHeight() {
        return surfaceHeight;
    }

    public void setSurfaceHeight(int surfaceHeight) {
        this.surfaceHeight = surfaceHeight;
    }

    public int getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public int getCurrentShapeColor() {
        return currentShapeColor;
    }

    public void setCurrentShapeColor(int currentShapeColor) {
        this.currentShapeColor = currentShapeColor;
    }

    public float getCurrentShapeWidth() {
        return currentShapeWidth;
    }

    public void setCurrentShapeWidth(float currentShapeWidth) {
        this.currentShapeWidth = currentShapeWidth;
    }

    public int getSurfaceBackgroundColor() {
        return surfaceBackgroundColor;
    }

    public void setSurfaceBackgroundColor(int surfaceBackgroundColor) {
        this.surfaceBackgroundColor = surfaceBackgroundColor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public final static class Shape {
        /**
         * 点
         */
        public final static int Point = 1;
        /**
         * 线
         */
        public final static int Line = 2;
        /**
         * 圆
         */
        public final static int Circle = 3;
        /**
         * 矩形
         */
        public final static int Rect = 4;
        /**
         * 图片
         */
        public final static int Image = 5;

        public final static int Text = 6;

    }
}
