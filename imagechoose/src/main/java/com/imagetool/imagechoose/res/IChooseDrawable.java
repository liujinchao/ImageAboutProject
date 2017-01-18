package com.imagetool.imagechoose.res;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.SparseArray;

/**
 * 类名称：IChooseDrawable
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/18 14:17
 * 描述：TODO
 */
public abstract class IChooseDrawable{

    private Paint paint;
    protected int width=0;
    protected int height=0;

    private SparseArray<Drawable> drawables;

    public IChooseDrawable(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0x88000000);
        drawables=new SparseArray<>();
    }

    public Drawable get(int state){
        if(drawables.indexOfKey(state)>=0){
            return drawables.get(state);
        }else{
            InDrawable drawable=new InDrawable(state);
            drawables.put(state,drawable);
            return drawable;
        }
    }

    public void clear(){
        drawables.clear();
    }

    public int getBaseline(Paint paint, int top, int bottom){
        Paint.FontMetrics i=paint.getFontMetrics();
        return (int) ((bottom+top-i.top-i.bottom)/2);
    }

    public abstract void draw(Canvas canvas, Paint paint, int state);

    private class InDrawable extends Drawable {

        private int state=0;

        InDrawable(int state){
            this.state=state;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            IChooseDrawable.this.draw(canvas,paint,state);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }
    }
}
