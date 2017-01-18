package com.imagetool.imagechoose.res;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 类名称：CircleChooseDrawable
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/1/18 14:24
 * 描述：图片选中指示器
 */
public class CircleChooseDrawable extends IChooseDrawable {

    private boolean isShowNum=true;
    private int chooseBgColor = 0xFFFF6600;
    private Path path;

    public CircleChooseDrawable(){
        super();
    }

    public CircleChooseDrawable(boolean isShowNum,int chooseBgColor){
        super();
        this.isShowNum=isShowNum;
        this.chooseBgColor=chooseBgColor;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int state) {
        width=canvas.getWidth();
        height=canvas.getHeight();
        if(state==0){
            paint.setColor(0x55000000);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2,height/2,width/2-2,paint);
            paint.setColor(0xDDFFFFFF);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(width/2,height/2,width/2-2,paint);
        }else{
            paint.setColor(chooseBgColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2,height/2,width/2-2,paint);
            paint.setColor(0xDDFFFFFF);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(width/2,height/2,width/2-2,paint);
            paint.setColor(0xDDFFFFFF);
            if(isShowNum){
                paint.setStyle(Paint.Style.FILL);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(width*0.53f);
                canvas.drawText(state+"",width/2,getBaseline(paint,0,height),paint);
            }else{
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                paint.setStrokeCap(Paint.Cap.ROUND);
                if(path==null){
                    path=new Path();
                    path.moveTo(width/4f,height/2f);
                    path.lineTo(width*2/5f,height*5/7f);
                    path.lineTo(width*3/4f,height/3f);
                }
                canvas.drawPath(path,paint);
            }
        }
    }

}
