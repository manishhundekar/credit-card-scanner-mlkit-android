package com.manish.creditcardscanner.charting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class MyGraphview extends View
{
    private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] value_degree;
    private String[] categories;
    private int[] COLORS={
            Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124),
            Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134),
            Color.rgb(179, 48, 80),
            Color.rgb(53, 194, 209)
    };
    RectF rectf = new RectF (10, 10, 750, 750);
    public MyGraphview(Context context, float[] inputValues, String[] categories) {
        super(context);
        float[] values = calculateData(inputValues);
        this.categories = categories;
        value_degree = new float[values.length];
        for(int i=0;i<values.length;i++)
        {
            value_degree[i]=values[i];
        }
    }

    private float[] calculateData(float[] data) {
        float total=0;
        for(int i=0;i<data.length;i++)
        {
            total+=data[i];
        }
        for(int i=0;i<data.length;i++)
        {
            data[i]=360*(data[i]/total);
        }
        return data;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int referenceFactor = 0;

        int centerX = (int) (rectf.left + rectf.right) / 2 - 65;
        int centerY = (int) ((rectf.top + rectf.bottom) / 2);
        int radius = (int) (rectf.right - rectf.left) / 2;

        radius *= 0.5;

        for (int i = 0; i < value_degree.length; i++)
        {
            if (i > 0)
                referenceFactor += (int) value_degree[i - 1];

            paint.setColor(COLORS[i]);
            canvas.drawArc(rectf, referenceFactor, value_degree[i], true, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25.0f);
            float medianAngle = (referenceFactor + (value_degree[i] / 2f)) * (float)Math.PI / 180f;
            canvas.drawText(categories[i], (float)(centerX + (radius * Math.cos(medianAngle))), (float)(centerY + (radius * Math.sin(medianAngle))), paint);
        }
    }
}
