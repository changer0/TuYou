package com.myxfd.tuyou.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by Lulu on 2016/11/6.
 */

public class CircleTransform implements Transformation {
    public static final String KEY = "picasso_circle";

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap ret = null;
        int minEdge = Math.min(source.getWidth(), source.getHeight());
        int dx = (source.getWidth() - minEdge) / 2;
        int dy = (source.getHeight() - minEdge) / 2;

        // Init shader
        Shader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate(-dx, -dy);   // Move the target area to center of the source bitmap
        shader.setLocalMatrix(matrix);

        // Init paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(shader);

        // Create and draw circle bitmap
        ret = Bitmap.createBitmap(minEdge, minEdge, source.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawOval(new RectF(0, 0, minEdge, minEdge), paint);
        // Recycle the source bitmap, because we already generate a new one
        source.recycle();

        return ret;
    }

    @Override
    public String key() {
        return KEY;
    }
}
