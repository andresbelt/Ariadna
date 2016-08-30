package com.oncreate.ariadna.UI.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.RelativeLayout;
import com.android.volley.DefaultRetryPolicy;

public class SquareRelativeLayout extends RelativeLayout {
    private float aspectRatio;
    private int maximumHeight;

    public SquareRelativeLayout(Context context) {
        super(context);
        this.aspectRatio = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.maximumHeight = 0;
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.aspectRatio = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.maximumHeight = 0;
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.aspectRatio = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.maximumHeight = 0;
    }

    @TargetApi(21)
    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.aspectRatio = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.maximumHeight = 0;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = widthMeasureSpec;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (this.aspectRatio != DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
            height = MeasureSpec.makeMeasureSpec((int) (((float) widthSize) / this.aspectRatio), widthMode);
        }
        if (this.maximumHeight > 0 && widthMode == 1073741824 && ((int) (((float) widthSize) / this.aspectRatio)) > this.maximumHeight) {
            height = MeasureSpec.makeMeasureSpec(this.maximumHeight, widthMode);
        }
        if (widthMode == LinearLayoutManager.INVALID_OFFSET) {
            height = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(height), 1073741824);
        }
        super.onMeasure(widthMeasureSpec, height);
    }

    public float getAspectRatio() {
        return this.aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setMaximumHeight(int maximumHeight) {
        this.maximumHeight = maximumHeight;
    }
}
