/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.laveen.exploreandrooid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * An {@link android.widget.ImageView} that draws its contents inside a mask and draws a border
 * drawable on top. This is useful for applying a beveled look to image contents, but is also
 * flexible enough for use with other desired aesthetics.
 */
public class BezelImageView extends ImageView {
    private Paint mBlackPaint;
    private Paint mMaskedPaint;
    private Paint mWhitePaint;


    private Rect mBounds;
    private RectF mBoundsF;

    private Drawable mBorderDrawable;
    private Drawable mMaskDrawable;

    private ColorMatrixColorFilter mDesaturateColorFilter;
    private boolean mDesaturateOnPress = false;

    private boolean mCacheValid = false;
    private Bitmap mCacheBitmap;
    private int mCachedWidth;
    private int mCachedHeight;

    public BezelImageView(Context context) {
        this(context, null);
    }

    public BezelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezelImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Attribute initialization
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BezelImageView,
                defStyle, 0);

        mMaskDrawable = a.getDrawable(R.styleable.BezelImageView_maskDrawable);
        if (mMaskDrawable != null) {
            mMaskDrawable.setCallback(this);
        }

        mBorderDrawable = a.getDrawable(R.styleable.BezelImageView_borderDrawable);
        if (mBorderDrawable != null) {
            mBorderDrawable.setCallback(this);
        }

        mDesaturateOnPress = a.getBoolean(R.styleable.BezelImageView_desaturateOnPress,
                mDesaturateOnPress);

        a.recycle();

        // Other initialization
        mBlackPaint = new Paint();
        mBlackPaint.setColor(0xff000000);

        mMaskedPaint = new Paint();
        mMaskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Always want a cache allocated.
        mCacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        if (mDesaturateOnPress) {
            // Create a desaturate color filter for pressed state.
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            mDesaturateColorFilter = new ColorMatrixColorFilter(cm);
        }

        mWhitePaint = new Paint();
        mWhitePaint.setStyle(Paint.Style.STROKE);
        mWhitePaint.setColor(0xffff0000);
        mWhitePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        mWhitePaint.setShadowLayer(6, 0, 0, Color.BLACK);
        mWhitePaint.setAntiAlias(true);
        //Paint paint = new Paint();
        mWhitePaint.setAlpha(254);
        setLayerType(LAYER_TYPE_SOFTWARE, mWhitePaint);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        final boolean changed = super.setFrame(l, t, r, b);
        int offset = (int) mWhitePaint.getStrokeWidth();
        mBounds = new Rect(0 + offset, 0 + offset, r - l-offset, b - t-offset);
        mBoundsF = new RectF(mBounds);

        if (mBorderDrawable != null) {
            mBorderDrawable.setBounds(mBounds);
        }
        if (mMaskDrawable != null) {
            mMaskDrawable.setBounds(mBounds);
        }

        if (changed) {
            mCacheValid = false;
        }

        return changed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBounds == null) {
            return;
        }

        int width = mBounds.width();
        int height = mBounds.height();

        if (width == 0 || height == 0) {
            return;
        }

        if (!mCacheValid || width != mCachedWidth || height != mCachedHeight) {
            // Need to redraw the cache
            if (width == mCachedWidth && height == mCachedHeight) {
                // Have a correct-sized bitmap cache already allocated. Just erase it.
                mCacheBitmap.eraseColor(0);
            } else {
                // Allocate a new bitmap with the correct dimensions.
                mCacheBitmap.recycle();
                //noinspection AndroidLintDrawAllocation
                mCacheBitmap = Bitmap.createBitmap((int) (getWidth()-2*mWhitePaint.getStrokeWidth()), (int) (getHeight()-2*mWhitePaint.getStrokeWidth()), Bitmap.Config.ARGB_8888);
                mCachedWidth = width;
                mCachedHeight = height;
            }

            Canvas cacheCanvas = new Canvas(mCacheBitmap);
            //mCacheBitmap.eraseColor(Color.BLUE);
            if (mMaskDrawable != null) {
               int sc = cacheCanvas.save();
                mMaskDrawable.setBounds(new Rect(0,0, (int) (getWidth()-2*mWhitePaint.getStrokeWidth()), (int) (getHeight()-2*mWhitePaint.getStrokeWidth())));
                mMaskDrawable.draw(cacheCanvas);
                mMaskedPaint.setColorFilter((mDesaturateOnPress && isPressed())
                        ? mDesaturateColorFilter : null);
                cacheCanvas.saveLayer(new RectF(0,0,getWidth()-2*mWhitePaint.getStrokeWidth(),getHeight()-2*mWhitePaint.getStrokeWidth()), mMaskedPaint,
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
                super.onDraw(cacheCanvas);
                cacheCanvas.restoreToCount(sc);
            } else if (mDesaturateOnPress && isPressed()) {
                int sc = cacheCanvas.save();
                cacheCanvas.drawRect(0, 0, mCachedWidth, mCachedHeight, mBlackPaint);
                mMaskedPaint.setColorFilter(mDesaturateColorFilter);
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint,
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
                super.onDraw(cacheCanvas);
                cacheCanvas.restoreToCount(sc);
            } else {
                super.onDraw(cacheCanvas);
            }

            if (mBorderDrawable != null) {
                mBorderDrawable.draw(cacheCanvas);
            }
        }

        // Draw from cache
        //canvas.drawBitmap(mCacheBitmap, mBounds.left, mBounds.top, null);

        canvas.drawBitmap(mCacheBitmap, mWhitePaint.getStrokeWidth(),mWhitePaint.getStrokeWidth(), null);
       // canvas.drawRect(0, 0, getWidth(), getHeight(), mWhitePaint);
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2-mWhitePaint.getStrokeWidth(),mWhitePaint);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mBorderDrawable != null && mBorderDrawable.isStateful()) {
            mBorderDrawable.setState(getDrawableState());
        }
        if (mMaskDrawable != null && mMaskDrawable.isStateful()) {
            mMaskDrawable.setState(getDrawableState());
        }
        if (isDuplicateParentStateEnabled()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (who == mBorderDrawable || who == mMaskDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(who);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mBorderDrawable || who == mMaskDrawable || super.verifyDrawable(who);
    }
}
