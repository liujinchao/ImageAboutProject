package com.imagetool.imagechoose.edit.paint;

import android.graphics.Canvas;
import android.graphics.Paint;

public class ImageShape extends PaintShape {


    public ImageShape(float mStartX, float mStartY, Paint mPaint) {
        super(mStartX, mStartY, mPaint);
    }

    public ImageShape(Paint paint) {
        super(paint);
    }

    @Override
    public void onDraw(Canvas canvas) {

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(PaintConfig.Shape.Image).append(FIELD_SEPARATOR);
        builder.append(getFieldString());
        return builder.toString();
    }
}
