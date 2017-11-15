package com.imagetool.imagechoose.edit.paint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;


public class TextShape extends PaintShape {

    private String content = null;

    public TextShape(Paint paint) {
        super(paint);

        content = PaintConfig.getInstance().getContent();
    }

    public TextShape(float mStartX, float mStartY, Paint mPaint) {
        super(mStartX, mStartY, mPaint);

        content = PaintConfig.getInstance().getContent();

        PaintConfig.getInstance().setContent( "" );
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.d("Text", "onDraw");

        TextPaint tp = new TextPaint();
        tp.setColor(Color.parseColor("#F30623"));
        tp.setStyle(Paint.Style.FILL);
        tp.setTextSize(50);

        float startX = mStartX;
        float endX = mEndX;

        if( startX > endX ) {
            float tmp = endX;
            endX = startX;
            startX = tmp;
        }

        float startY = mStartY;
        float endY = mEndY;
        if( startY > endY ) {
            float tmp = endY;
            endY = startY;
            startY = tmp;
        }

        drawTextCenter( content, tp, canvas, new Point( (int)startX, (int)startY), (int)(endX - startX), Layout.Alignment.ALIGN_CENTER,1.5f,0,false );

        // canvas.drawText( content, mStartX, mStartY, mEndX, mEndY, mPaint));
        // canvas.drawRect(mStartX, mStartY, mEndX, mEndY, mPaint);
    }

    private void drawTextCenter(String string, TextPaint textPaint, Canvas canvas, Point point, int width,
                                Layout.Alignment align, float spacingMulti, float spacingAdd, boolean includePad){

        Log.d("Text", "point = " + point);

        StaticLayout staticLayout = new StaticLayout(string,textPaint,width, align,spacingMulti,spacingAdd,includePad);
        canvas.save();
        canvas.translate(-staticLayout.getWidth()/2+point.x,-staticLayout.getHeight()/2+point.y);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(PaintConfig.Shape.Rect).append(FIELD_SEPARATOR);
        builder.append(getFieldString());
        return builder.toString();
    }

}
