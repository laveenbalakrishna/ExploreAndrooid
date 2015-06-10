package com.example.laveen.exploreandrooid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by laveen on 8/6/15.
 */
public class CircleView extends ImageView {
    Paint paint;
    Paint bitmapPaint;
    Bitmap circleBimap;
    Bitmap squareBitmap;
    Bitmap bitmap;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Canvas canvas1;

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setShadowLayer(6, 0, 0, Color.BLACK);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        circleBimap = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        squareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue);

        bitmapPaint = new Paint();
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //setBackgroundColor(Color.YELLOW);
        //canvas1.drawBitmap(squareBitmap, 0, 0, bitmapPaint);
        //circleBimap.recycle();
        //squareBitmap.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("CANVAS", "REF  :" + canvas);

        Bitmap bitmap1 = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);


        Canvas canvas2 = new Canvas(bitmap1);

       /* Drawable circle = getResources().getDrawable(R.drawable.mask);
        circle.setBounds(0, 0, getWidth(), getHeight());
        circle.draw(canvas2);

        int sc = canvas2.save();
        canvas2.saveLayer(new RectF(0, 0, getWidth(), getHeight()), bitmapPaint,
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
        super.onDraw(canvas2);
        canvas.drawARGB(255,255,255,255);
        canvas2.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2-paint.getStrokeWidth(),paint);
        canvas2.restoreToCount(sc);*/

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(circleBimap,getWidth(),getHeight(),false);
        canvas2.drawBitmap(scaledBitmap,0,0,null);

        canvas.drawBitmap(bitmap1, 0, 0, null);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        Log.d("IMAGE", "CHANGED  :" + changed);
        Log.d("IMAGE", "LEFT  :" + l);
        Log.d("IMAGE", "TOP   :" + t);
        Log.d("IMAGE", "RIGHT :" + r);
        Log.d("IMAGE", "BOTTOM:" + b + "\n\n\n");
        return changed;
    }
}
