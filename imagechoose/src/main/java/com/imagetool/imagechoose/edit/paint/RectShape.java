package com.imagetool.imagechoose.edit.paint;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectShape extends PaintShape {


    public RectShape(float mStartX, float mStartY, Paint mPaint) {
        super(mStartX, mStartY, mPaint);
    }

    public RectShape(Paint paint) {
        super(paint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(mStartX, mStartY, mEndX, mEndY, mPaint);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(PaintConfig.Shape.Rect).append(FIELD_SEPARATOR);
        builder.append(getFieldString());
        return builder.toString();
    }
}
