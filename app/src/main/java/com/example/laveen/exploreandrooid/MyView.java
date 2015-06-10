package com.example.laveen.exploreandrooid;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by laveen on 2/6/15.
 */
public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d("VIEW", "HEIGHT: " + height);
        Log.d("VIEW", "MODE: " + MeasureSpec.getMode(heightMeasureSpec));
     //   setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),800);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
